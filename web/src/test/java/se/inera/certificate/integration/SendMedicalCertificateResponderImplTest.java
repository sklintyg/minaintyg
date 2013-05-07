package se.inera.certificate.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import iso.v21090.dt.v1.II;

import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3.wsaddressing10.AttributedURIType;

import riv.insuranceprocess.healthreporting.medcertqa._1.LakarutlatandeEnkelType;
import se.inera.certificate.integration.certificates.CertificateSupport;
import se.inera.certificate.integration.certificates.fk7263.Fk7263Support;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificate.v1.rivtabp20.SendMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;

@RunWith(MockitoJUnitRunner.class)
public class SendMedicalCertificateResponderImplTest {

    @Mock
    private CertificateService certificateService = mock(CertificateService.class);

    @Mock
    private Certificate certificate = mock(Certificate.class);
    
    @Mock
    private RegisterMedicalCertificateResponderInterface registerMedicalCertificateResponderInterface = mock(RegisterMedicalCertificateResponderInterface.class);

    @Mock
    private List<CertificateSupport> supportedCertificates;
    
    @InjectMocks
    private SendMedicalCertificateResponderInterface responder = new SendMedicalCertificateResponderImpl();

    @Before
    public void before() {
        when(supportedCertificates.iterator()).thenReturn(Collections.<CertificateSupport>singletonList(new Fk7263Support()).iterator());
    }
    
    @Test
    public void testGet() throws Exception {
    	SendMedicalCertificateRequestType parameters = new SendMedicalCertificateRequestType();
    	parameters.setSend(new SendType());
    	parameters.getSend().setLakarutlatande(new LakarutlatandeEnkelType());
    	parameters.getSend().getLakarutlatande().setLakarutlatandeId("Intygs-id-1234567890");
    	parameters.getSend().getLakarutlatande().setPatient(new PatientType());
    	parameters.getSend().getLakarutlatande().getPatient().setPersonId(new II());
    	parameters.getSend().getLakarutlatande().getPatient().getPersonId().setExtension("19121212-1212");
    	
    	when(certificateService.getCertificate("19121212-1212", "Intygs-id-1234567890")).thenReturn(certificate);
    	when(certificate.getType()).thenReturn("fk7263");
    	
    	String document = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("fk7263/fk7263.xml"));   	
    	when(certificate.getDocument()).thenReturn(document);
    	
    	AttributedURIType address = new AttributedURIType();
    	address.setValue("EnAdress");
		responder.sendMedicalCertificate(address, parameters);
		
		ArgumentCaptor<AttributedURIType> logicalAddress = ArgumentCaptor.forClass(AttributedURIType.class);
		ArgumentCaptor<RegisterMedicalCertificateType> type = ArgumentCaptor.forClass(RegisterMedicalCertificateType.class);  
		
		verify(registerMedicalCertificateResponderInterface).registerMedicalCertificate(logicalAddress.capture(), type.capture());
		
		assertEquals("EnAdress",logicalAddress.getValue().getValue());
		assertNotNull(type.getValue());
	}

}
