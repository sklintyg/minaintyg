package se.inera.certificate.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.certificate.dao.CertificateDao;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.CertificateStateHistoryEntry;
import se.inera.certificate.service.CertificateService;
import se.inera.certificate.service.ConsentService;

@RunWith(MockitoJUnitRunner.class)
public class CertificateServiceImplTest {

    @Mock
    private CertificateDao certificateDao = mock(CertificateDao.class);
    
    @Mock
    private ConsentService consentService = mock(ConsentService.class);

    @InjectMocks
    private CertificateService certificateService = new CertificateServiceImpl();

    @Test
    public void certificateWithDeletedStatusHasMetaDeleted() {
        Certificate certificate = new Certificate("certificateId", "document");
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.DELETED, new LocalDateTime(1)));
        when(consentService.isConsent(anyString())).thenReturn(Boolean.TRUE);
        when(certificateDao.getCertificate(anyString())).thenReturn(certificate );
        
        Certificate found = certificateService.getCertificate("civicRegistrationNumber", "certificateId");
        assertTrue(found.getDeleted());
    }

    @Test
    public void certificateWithStatusRestoredNewerThanDeletedHasMetaNotDeleted() {
        Certificate certificate = new Certificate("certificateId", "document");
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.RESTORED, new LocalDateTime(2)));
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.DELETED, new LocalDateTime(1)));
        when(consentService.isConsent(anyString())).thenReturn(Boolean.TRUE);
        when(certificateDao.getCertificate(anyString())).thenReturn(certificate );
        
        Certificate found = certificateService.getCertificate("civicRegistrationNumber", "certificateId");
        assertFalse(found.getDeleted());
    }

    @Test
    public void certificateWithStatusDeletedNewerThanRestoredHasMetaDeleted() {
        Certificate certificate = new Certificate("certificateId", "document");
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.DELETED, new LocalDateTime(2)));
        certificate.getStates().add(new CertificateStateHistoryEntry("", CertificateState.RESTORED, new LocalDateTime(1)));
        when(consentService.isConsent(anyString())).thenReturn(Boolean.TRUE);
        when(certificateDao.getCertificate(anyString())).thenReturn(certificate );
        
        Certificate found = certificateService.getCertificate("civicRegistrationNumber", "certificateId");
        assertTrue(found.getDeleted());
    }

}
