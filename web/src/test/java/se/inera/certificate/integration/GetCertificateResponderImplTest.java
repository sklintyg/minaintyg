package se.inera.certificate.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.certificate.model.CertificateMetaData;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificate.v1.rivtabp20.GetCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateResponseType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.OK;

/**
 * @author andreaskaltenbach
 */
@RunWith(MockitoJUnitRunner.class)
public class GetCertificateResponderImplTest {

    @Mock
    private CertificateService certificateService = mock(CertificateService.class);

    @InjectMocks
    private GetCertificateResponderInterface responder = new GetCertificateResponderImpl();

    @Test
    public void getCertificate() {
        String civicRegistrationNumber = "19350108-1234";
        String certificateId = "123456";

        when(certificateService.getCertificate(civicRegistrationNumber, certificateId)).thenReturn(new CertificateMetaData());

        GetCertificateRequestType parameters = createGetCertificateRequest(civicRegistrationNumber, certificateId);

        GetCertificateResponseType response = responder.getCertificate(null, parameters);

        assertNotNull(response.getMeta());
        assertEquals(OK, response.getResult().getResultCode());
    }

    private GetCertificateRequestType createGetCertificateRequest(String civicRegistrationNumber, String certificateId) {
        GetCertificateRequestType parameters = new GetCertificateRequestType();
        parameters.setNationalIdentityNumber(civicRegistrationNumber);
        parameters.setCertificateId(certificateId);
        return parameters;
    }
}
