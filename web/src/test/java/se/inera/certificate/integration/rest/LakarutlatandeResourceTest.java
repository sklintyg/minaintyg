package se.inera.certificate.integration.rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.service.CertificateService;

import javax.ws.rs.core.Response;

@RunWith(MockitoJUnitRunner.class)
public class LakarutlatandeResourceTest {

    @Mock
    private CertificateService certificateService = mock(CertificateService.class);
    
    @InjectMocks
    LakarutlatandeResource resource = new LakarutlatandeResource();
    
    @Test
    public void knownIdResolvesIntoLakarutlatandeJson() {
        when(certificateService.getCertificate(null, "knownid")).thenReturn(new Certificate("knownid", "{data: 1}"));
        Response response = resource.getCertificate("knownid");
        assertEquals(200, response.getStatus());
        assertEquals("{data: 1}", response.getEntity());
    }

    @Test
    public void unknownIdResolvesIntoNull() {
        Response response= resource.getCertificate("knownid");
        assertEquals(404, response.getStatus());
    }

}
