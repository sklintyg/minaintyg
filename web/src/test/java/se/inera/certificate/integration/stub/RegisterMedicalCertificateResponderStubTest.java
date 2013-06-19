package se.inera.certificate.integration.stub;

import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import iso.v21090.dt.v1.II;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificate;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;

@RunWith(MockitoJUnitRunner.class)
public class RegisterMedicalCertificateResponderStubTest {
    
    @Mock
    FkMedicalCertificatesStore store;
    
    @InjectMocks
    RegisterMedicalCertificateResponderStub stub = new RegisterMedicalCertificateResponderStub();
    
    @SuppressWarnings("unchecked")
    @Test
    public void testName() throws Exception {
        AttributedURIType logicalAddress = new AttributedURIType();
        RegisterMedicalCertificate request = new RegisterMedicalCertificate();
        Lakarutlatande lakarutlatande = new Lakarutlatande();
        lakarutlatande.setLakarutlatandeId("id-1234567890");
        PatientType patient = new PatientType();
        II iiid = new II();
        iiid.setExtension("19121212-1212");
        patient.setPersonId(iiid);
        lakarutlatande.setPatient(patient);
        request.setLakarutlatande(lakarutlatande);
        
        stub.registerMedicalCertificate(logicalAddress, request);
        
        verify(store).addCertificate(eq("id-1234567890"), any(Map.class));
    }
}
