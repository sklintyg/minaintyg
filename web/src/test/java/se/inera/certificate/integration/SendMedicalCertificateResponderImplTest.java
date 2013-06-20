package se.inera.certificate.integration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.OK;

import iso.v21090.dt.v1.II;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import riv.insuranceprocess.healthreporting.medcertqa._1.LakarutlatandeEnkelType;
import se.inera.certificate.model.builder.CertificateBuilder;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificate.v1.rivtabp20.SendMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;

@RunWith( MockitoJUnitRunner.class )
public class SendMedicalCertificateResponderImplTest {

    private static final String CERTIFICATE_ID = "Intygs-id-1234567890";
    private static final String PERSONNUMMER = "19121212-1212";

    @Mock
    private CertificateService certificateService = mock(CertificateService.class);

    @InjectMocks
    private SendMedicalCertificateResponderInterface responder = new SendMedicalCertificateResponderImpl();

    @Test
    public void testSend() {
        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(new CertificateBuilder(CERTIFICATE_ID).build());
        SendMedicalCertificateResponseType response = responder.sendMedicalCertificate(null, createRequest());

        assertEquals(OK, response.getResult().getResultCode());
        verify(certificateService).sendCertificate(PERSONNUMMER, CERTIFICATE_ID, "FK");
    }

    private SendMedicalCertificateRequestType createRequest() {
        SendMedicalCertificateRequestType request = new SendMedicalCertificateRequestType();
        request.setSend(new SendType());
        request.getSend().setLakarutlatande(new LakarutlatandeEnkelType());
        request.getSend().getLakarutlatande().setLakarutlatandeId(CERTIFICATE_ID);
        request.getSend().getLakarutlatande().setPatient(new PatientType());
        request.getSend().getLakarutlatande().getPatient().setPersonId(new II());
        request.getSend().getLakarutlatande().getPatient().getPersonId().setExtension(PERSONNUMMER);
        return request;
    }
}
