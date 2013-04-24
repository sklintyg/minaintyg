package se.inera.certificate.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.certificate.integration.certificates.fk7263.Fk7263Support;
import se.inera.certificate.model.builder.CertificateMetaDataBuilder;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ErrorIdEnum;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.*;

/**
 * @author andreaskaltenbach
 */
@RunWith(MockitoJUnitRunner.class)
public class GetCertificateResponderImplTest {

    @Mock
    private CertificateService certificateService = mock(CertificateService.class);

    @InjectMocks
    private GetCertificateResponderImpl responder = new GetCertificateResponderImpl();

    @Before
    public void initializeCertificateSupport() {
        responder.supportedCertificates.add(new Fk7263Support());
    }

    @Test
    public void getCertificate() {
        String civicRegistrationNumber = "19350108-1234";
        String certificateId = "123456";

        when(certificateService.getCertificate(civicRegistrationNumber, certificateId)).thenReturn(
                new CertificateMetaDataBuilder("123456").certificateType("fk7263").build());

        GetCertificateRequestType parameters = createGetCertificateRequest(civicRegistrationNumber, certificateId);

        GetCertificateResponseType response = responder.getCertificate(null, parameters);

        assertNotNull(response.getMeta());
        assertEquals(OK, response.getResult().getResultCode());
    }

    @Test
    public void getCertificateWithUnknownCertificateId() {
        String civicRegistrationNumber = "19350108-1234";
        String certificateId = "unknownId";

        when(certificateService.getCertificate(civicRegistrationNumber, certificateId)).thenReturn(null);

        GetCertificateRequestType parameters = createGetCertificateRequest(civicRegistrationNumber, certificateId);

        GetCertificateResponseType response = responder.getCertificate(null, parameters);

        assertNull(response.getMeta());
        assertNull(response.getCertificate());
        assertEquals(ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Unknown certificate ID: unknownId", response.getResult().getErrorText());
    }

    @Test
    public void getCertificateWithUnsupportedCertificateType() {
        String civicRegistrationNumber = "19350108-1234";
        String certificateId = "123456";

        when(certificateService.getCertificate(civicRegistrationNumber, certificateId)).thenReturn(
                        new CertificateMetaDataBuilder("123456").certificateType("unsupportedCertificateType").build());


        GetCertificateRequestType parameters = createGetCertificateRequest(civicRegistrationNumber, certificateId);

        GetCertificateResponseType response = responder.getCertificate(null, parameters);

        assertNotNull(response.getMeta());
        assertNull(response.getCertificate());
        assertEquals(ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.APPLICATION_ERROR, response.getResult().getErrorId());
        assertEquals("Unsupported certificate type: unsupportedCertificateType", response.getResult().getErrorText());
    }

    private GetCertificateRequestType createGetCertificateRequest(String civicRegistrationNumber, String certificateId) {
        GetCertificateRequestType parameters = new GetCertificateRequestType();
        parameters.setNationalIdentityNumber(civicRegistrationNumber);
        parameters.setCertificateId(certificateId);
        return parameters;
    }
}
