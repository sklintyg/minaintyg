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
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificate.v1.rivtabp20.RevokeMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;

@RunWith(MockitoJUnitRunner.class)
public class RevokeMedicalCertificateResponderImplTest {

    @Mock
    private CertificateService certificateService = mock(CertificateService.class);

    @InjectMocks
    private RevokeMedicalCertificateResponderInterface responder = new RevokeMedicalCertificateResponderImpl();

    @Test
    public void testRevoke() throws Exception {
    	
    	RevokeMedicalCertificateRequestType parameters = new RevokeMedicalCertificateRequestType();
    	parameters.setRevoke(new RevokeType());
    	parameters.getRevoke().setLakarutlatande(new LakarutlatandeEnkelType());
    	parameters.getRevoke().getLakarutlatande().setLakarutlatandeId("Intygs-id-1234567890");
    	parameters.getRevoke().getLakarutlatande().setPatient(new PatientType());
    	parameters.getRevoke().getLakarutlatande().getPatient().setPersonId(new II());
    	parameters.getRevoke().getLakarutlatande().getPatient().getPersonId().setExtension("19121212-1212");
    	
    	Certificate certificate = new Certificate("intigsid-1234567890", "text");
    	
    	when(certificateService.getCertificate("19121212-1212", "Intygs-id-1234567890")).thenReturn(certificate);
    	
		responder.revokeMedicalCertificate(null, parameters);
		
	}
}
