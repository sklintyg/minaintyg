package se.inera.certificate.service.impl;

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
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.CertificateStateHistoryEntry;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.service.CertificateService;
import se.inera.certificate.service.ConsentService;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

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

    @InjectMocks
    private CertificateService certificateService = new CertificateServiceImpl();

    @Test
    public void certificateWithDeletedStatusHasMetaDeleted() {
        Certificate certificate = new Certificate("certificateId", "document");
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.DELETED, new LocalDateTime(1)));
        when(consentService.isConsent(anyString())).thenReturn(Boolean.TRUE);
        when(certificateDao.getCertificate(anyString())).thenReturn(certificate);

        Certificate found = certificateService.getCertificate("civicRegistrationNumber", "certificateId");
        assertTrue(found.getDeleted());
    }

    @Test
    public void certificateWithStatusRestoredNewerThanDeletedHasMetaNotDeleted() {
        Certificate certificate = new Certificate("certificateId", "document");
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.RESTORED, new LocalDateTime(2)));
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.DELETED, new LocalDateTime(1)));
        when(consentService.isConsent(anyString())).thenReturn(Boolean.TRUE);
        when(certificateDao.getCertificate(anyString())).thenReturn(certificate);

        Certificate found = certificateService.getCertificate("civicRegistrationNumber", "certificateId");
        assertFalse(found.getDeleted());
    }

    @Test
    public void certificateWithStatusDeletedNewerThanRestoredHasMetaDeleted() {
        Certificate certificate = new Certificate("certificateId", "document");
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.DELETED, new LocalDateTime(2)));
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.RESTORED, new LocalDateTime(1)));
        when(consentService.isConsent(anyString())).thenReturn(Boolean.TRUE);
        when(certificateDao.getCertificate(anyString())).thenReturn(certificate);

        Certificate found = certificateService.getCertificate("civicRegistrationNumber", "certificateId");
        assertTrue(found.getDeleted());
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
}
