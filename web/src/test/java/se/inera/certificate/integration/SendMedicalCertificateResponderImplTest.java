package se.inera.certificate.integration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import iso.v21090.dt.v1.II;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import riv.insuranceprocess.healthreporting.medcertqa._1.LakarutlatandeEnkelType;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificate.v1.rivtabp20.SendMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;

@RunWith(MockitoJUnitRunner.class)
public class SendMedicalCertificateResponderImplTest {

    @Mock
    private CertificateService certificateService = mock(CertificateService.class);

    @InjectMocks
    private SendMedicalCertificateResponderInterface responder = new SendMedicalCertificateResponderImpl();

    @Test
    public void testGet() throws Exception {
    	SendMedicalCertificateRequestType parameters = new SendMedicalCertificateRequestType();
    	parameters.setSend(new SendType());
    	parameters.getSend().setLakarutlatande(new LakarutlatandeEnkelType());
    	parameters.getSend().getLakarutlatande().setLakarutlatandeId("Intygs-id-1234567890");
    	parameters.getSend().getLakarutlatande().setPatient(new PatientType());
    	parameters.getSend().getLakarutlatande().getPatient().setPersonId(new II());
    	parameters.getSend().getLakarutlatande().getPatient().getPersonId().setExtension("19121212-1212");
    	
    	Certificate certificate = new Certificate("intigsid-1234567890", "text");
    	
    	when(certificateService.getCertificate("19121212-1212", "Intygs-id-1234567890")).thenReturn(certificate);
    	
		responder.sendMedicalCertificate(null, parameters);
	}

}
