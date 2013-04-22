package se.inera.certificate.integration;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.certificate.service.CertificateService;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesResponseType;

@RunWith(MockitoJUnitRunner.class)
public class ListCertificatesResponderImplTest {

    @Mock
    private CertificateService certificateService = mock(CertificateService.class);
    
    @InjectMocks
    private ListCertificatesResponderImpl sut = new ListCertificatesResponderImpl();
    
    @Test
    public void listAnyCertificatesForUnkownPersonGivesEmptyList() {
        AttributedURIType logicalAddress = null;
        ListCertificatesRequestType parameters = new ListCertificatesRequestType();
        parameters.setNationalIdentityNumber("unknown-person");
        ListCertificatesResponseType result = sut.listCertificates(logicalAddress, parameters);
        assertTrue(result.getMeta().isEmpty());
    }

}
