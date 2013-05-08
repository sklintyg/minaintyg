package se.inera.certificate.service.impl;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.certificate.dao.CertificateDao;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.service.CertificateService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author andreaskaltenbach
 */
@RunWith( MockitoJUnitRunner.class )
public class CertificateServiceImplTest {

    @Mock
    private CertificateDao certificateDao = mock(CertificateDao.class);

    @InjectMocks
    private CertificateService certificateService = new CertificateServiceImpl();

    @Test
    public void testStoreCertificate() {

        Certificate certificate = new Certificate("certId", "<cert/>");
        certificateService.storeCertificate(certificate);

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
