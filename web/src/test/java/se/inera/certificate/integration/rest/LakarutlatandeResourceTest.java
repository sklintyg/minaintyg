package se.inera.certificate.integration.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.certificate.model.Certificate;
import se.inera.certificate.service.CertificateService;

@RunWith(MockitoJUnitRunner.class)
public class LakarutlatandeResourceTest {

    @Mock
    private CertificateService certificateService = mock(CertificateService.class);
    
    @InjectMocks
    LakarutlatandeResource resource = new LakarutlatandeResource();
    
    @Test
    public void knownIdResolvesIntoLakarutlatandeJson() {
        when(certificateService.getCertificate(null, "knownid")).thenReturn(new Certificate("knownid", "{data: 1}"));
        String lakarutlatandeJson = resource.getCertificate("knownid");
        assertEquals("{data: 1}", lakarutlatandeJson);
    }

    @Test
    public void unknownIdResolvesIntoNull() {
        String lakarutlatandeJson = resource.getCertificate("knownid");
        assertNull(lakarutlatandeJson);
    }

}
