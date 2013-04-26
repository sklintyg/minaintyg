package se.inera.certificate.integration;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import riv.insuranceprocess.healthreporting.setcertificatestatus._1.rivtabp20.SetCertificateStatusResponderInterface;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatusresponder.v1.SetCertificateStatusRequestType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author andreaskaltenbach
 */
@RunWith(MockitoJUnitRunner.class)
public class SetCertificateStatusResponderImplTest {

    @Mock
    private CertificateService certificateService = mock(CertificateService.class);

    @InjectMocks
    private SetCertificateStatusResponderInterface responder = new SetCertificateStatusResponderImpl();

    @Test
    public void testSetCertificateStatus() {

        LocalDateTime timestamp = new LocalDateTime(2013, 4, 26, 12, 0, 0);

        SetCertificateStatusRequestType request = new SetCertificateStatusRequestType();
        request.setCertificateId("no5");
        request.setNationalIdentityNumber("19001122-3344");
        request.setStatus(StatusType.DELETED);
        request.setTarget("försäkringskassan");
        request.setTimestamp(timestamp);

        responder.setCertificateStatus(null, request);

        verify(certificateService).setCertificateState("19001122-3344", "no5", "försäkringskassan", CertificateState.DELETED, timestamp);
    }
}
