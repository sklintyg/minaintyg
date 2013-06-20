package se.inera.certificate.service.impl;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import se.inera.certificate.dao.CertificateDao;
import se.inera.certificate.exception.CertificateRevokedException;
import se.inera.certificate.exception.InvalidCertificateException;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.CertificateStateHistoryEntry;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.model.builder.CertificateBuilder;
import se.inera.certificate.service.CertificateSenderService;
import se.inera.certificate.service.CertificateService;
import se.inera.certificate.service.ConsentService;

/**
 * @author andreaskaltenbach
 */
@RunWith( MockitoJUnitRunner.class )
public class CertificateServiceImplTest {

    private static final String PERSONNUMMER = "<civicRegistrationNumber>";
    private static final String CERTIFICATE_ID = "<certificate-id>";

    @Mock
    private CertificateDao certificateDao = mock(CertificateDao.class);

    @Mock
    private ConsentService consentService = mock(ConsentService.class);

    @Mock
    private ObjectMapper objectMapper = mock(ObjectMapper.class);

    @Mock
    private CertificateSenderService certificateSender = mock(CertificateSenderService.class);

    @InjectMocks
    private CertificateService certificateService = new CertificateServiceImpl();

    @Test
    public void certificateWithDeletedStatusHasMetaDeleted() {
        Certificate certificate = createCertificate();
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.DELETED, new LocalDateTime(1)));
        when(consentService.isConsent(anyString())).thenReturn(Boolean.TRUE);
        when(certificateDao.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(certificate);

        Certificate found = certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID);

        assertTrue(found.getDeleted());

        verify(certificateDao).getCertificate(PERSONNUMMER, CERTIFICATE_ID);
    }

    private Certificate createCertificate() {
        Certificate certificate = new Certificate(CERTIFICATE_ID, "document");
        certificate.setCivicRegistrationNumber(PERSONNUMMER);
        return certificate;
    }

    @Test
    public void certificateWithStatusRestoredNewerThanDeletedHasMetaNotDeleted() {
        Certificate certificate = createCertificate();
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.RESTORED, new LocalDateTime(2)));
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.DELETED, new LocalDateTime(1)));
        when(consentService.isConsent(anyString())).thenReturn(Boolean.TRUE);
        when(certificateDao.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(certificate);

        Certificate found = certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID);
        assertFalse(found.getDeleted());

        verify(certificateDao).getCertificate(PERSONNUMMER, CERTIFICATE_ID);
    }

    @Test
    public void certificateWithStatusDeletedNewerThanRestoredHasMetaDeleted() {
        Certificate certificate = createCertificate();
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.DELETED, new LocalDateTime(2)));
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.RESTORED, new LocalDateTime(1)));
        when(consentService.isConsent(anyString())).thenReturn(Boolean.TRUE);
        when(certificateDao.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(certificate);

        Certificate found = certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID);
        assertTrue(found.getDeleted());

        verify(certificateDao).getCertificate(PERSONNUMMER, CERTIFICATE_ID);
    }

    private Lakarutlatande lakarutlatande() throws IOException {
        ObjectMapper customObjectMapper = new CustomObjectMapper();
        InputStream inputStream = new ClassPathResource("lakarutlatande/lakarutlatande.json").getInputStream();
        return customObjectMapper.readValue(inputStream, Lakarutlatande.class);
    }

    @Test
    public void testStoreCertificateExtractsCorrectInfo() throws IOException {

        when(objectMapper.writeValueAsString(any(Lakarutlatande.class))).thenReturn("Some JSON");

        Certificate certificate = certificateService.storeCertificate(lakarutlatande());

        assertEquals("1", certificate.getId());
        assertEquals("fk7263", certificate.getType());
        assertNotNull(certificate.getDocument());
        assertEquals("Hans Rosling", certificate.getSigningDoctorName());
        assertEquals("Vårdcentrum i väst", certificate.getCareUnitName());
        assertEquals("19001122-3344", certificate.getCivicRegistrationNumber());
        assertEquals(new LocalDateTime("2013-05-31T09:51:38.570"), certificate.getSignedDate());
        assertEquals(new LocalDate("2013-06-01"), certificate.getValidFromDate());
        assertEquals(new LocalDate("2013-06-12"), certificate.getValidToDate());

        assertEquals("Some JSON", certificate.getDocument());

        verify(objectMapper).writeValueAsString(any(Lakarutlatande.class));
        verify(certificateDao).store(certificate);
    }

    @Test
    public void newCertificateGetsStatusReceived() throws IOException {

        Certificate certificate = certificateService.storeCertificate(lakarutlatande());

        assertEquals(1, certificate.getStates().size());
        assertEquals(CertificateState.RECEIVED, certificate.getStates().get(0).getState());
        assertEquals("MI", certificate.getStates().get(0).getTarget());

        LocalDateTime aMinuteAgo = new LocalDateTime().minusMinutes(1);
        LocalDateTime inAMinute = new LocalDateTime().plusMinutes(1);
        assertTrue(certificate.getStates().get(0).getTimestamp().isAfter(aMinuteAgo));
        assertTrue(certificate.getStates().get(0).getTimestamp().isBefore(inAMinute));

        verify(certificateDao).store(certificate);
    }

    @Test
    public void sendCertificateCallsSenderAndSetsStatus() throws IOException {

        Certificate certificate = new CertificateBuilder(CERTIFICATE_ID)
                .civicRegistrationNumber(PERSONNUMMER)
                .build();

        when(certificateDao.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(certificate);

        certificateService.sendCertificate(PERSONNUMMER, CERTIFICATE_ID, "fk");

        verify(certificateDao).getCertificate(PERSONNUMMER, CERTIFICATE_ID);
        verify(certificateDao).updateStatus(CERTIFICATE_ID, PERSONNUMMER, CertificateState.SENT, "fk", null);
        verify(certificateSender).sendCertificate(certificate, "fk");
    }

    @Test( expected = InvalidCertificateException.class )
    public void testSendCertificateWitUnknownCertificate() {
        when(certificateDao.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(null);

        certificateService.sendCertificate(PERSONNUMMER, CERTIFICATE_ID, "fk");
    }

    @Test ( expected = CertificateRevokedException.class )
    public void testSendRevokedCertificate() {
        Certificate revokedCertificate = new CertificateBuilder(CERTIFICATE_ID)
                .state(CertificateState.CANCELLED, null)
                .build();

        when(certificateDao.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(revokedCertificate);

        certificateService.sendCertificate(PERSONNUMMER, CERTIFICATE_ID, "fk");
    }
}
