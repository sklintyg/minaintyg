package se.inera.certificate.service.impl;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import se.inera.certificate.dao.CertificateDao;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.CertificateStateHistoryEntry;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.service.CertificateSenderService;
import se.inera.certificate.service.CertificateService;
import se.inera.certificate.service.ConsentService;

/**
 * @author andreaskaltenbach
 */
@RunWith(MockitoJUnitRunner.class)
public class CertificateServiceImplTest {

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
        when(certificateDao.getCertificate("<civicRegistrationNumber>", "<certificateId>")).thenReturn(certificate);

        Certificate found = certificateService.getCertificate("<civicRegistrationNumber>", "<certificateId>");

        assertTrue(found.getDeleted());

        verify(certificateDao).getCertificate("<civicRegistrationNumber>", "<certificateId>");
    }

    private Certificate createCertificate() {
        Certificate certificate = new Certificate("<certificateId>", "document");
        certificate.setCivicRegistrationNumber("<civicRegistrationNumber>");
        return certificate;
    }

    @Test
    public void certificateWithStatusRestoredNewerThanDeletedHasMetaNotDeleted() {
        Certificate certificate = createCertificate();
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.RESTORED, new LocalDateTime(2)));
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.DELETED, new LocalDateTime(1)));
        when(consentService.isConsent(anyString())).thenReturn(Boolean.TRUE);
        when(certificateDao.getCertificate("<civicRegistrationNumber>", "<certificateId>")).thenReturn(certificate);

        Certificate found = certificateService.getCertificate("<civicRegistrationNumber>", "<certificateId>");
        assertFalse(found.getDeleted());

        verify(certificateDao).getCertificate("<civicRegistrationNumber>", "<certificateId>");
    }

    @Test
    public void certificateWithStatusDeletedNewerThanRestoredHasMetaDeleted() {
        Certificate certificate = createCertificate();
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.DELETED, new LocalDateTime(2)));
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.RESTORED, new LocalDateTime(1)));
        when(consentService.isConsent(anyString())).thenReturn(Boolean.TRUE);
        when(certificateDao.getCertificate("<civicRegistrationNumber>", "<certificateId>")).thenReturn(certificate);

        Certificate found = certificateService.getCertificate("<civicRegistrationNumber>", "<certificateId>");
        assertTrue(found.getDeleted());

        verify(certificateDao).getCertificate("<civicRegistrationNumber>", "<certificateId>");
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
        Lakarutlatande lakarutlatande = lakarutlatande();
        String personnummer = lakarutlatande.getPatient().getId();
        String utlatandeid = lakarutlatande.getId();

        Certificate certificate = certificateService.storeCertificate(lakarutlatande);
        certificate.setCivicRegistrationNumber("<civicRegistrationNumber>");
        when(certificateDao.getCertificate(personnummer, utlatandeid)).thenReturn(certificate);
        ArgumentCaptor<Certificate> certificateCaptor = ArgumentCaptor.forClass(Certificate.class);

        certificateService.sendCertificate(personnummer, utlatandeid, "FK");

        verify(certificateDao).getCertificate(personnummer,utlatandeid);
        verify(certificateDao).updateStatus(eq(utlatandeid), eq(personnummer), eq(CertificateState.SENT), eq("FK"), any(LocalDateTime.class));
        verify(certificateSender).sendCertificate(certificateCaptor.capture(), eq("FK"));
        assertEquals(certificate.getId(), certificateCaptor.getValue().getId());
    }
}
