package se.inera.certificate.integration.rest;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.exception.MissingConsentException;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.Utlatande;
import se.inera.certificate.model.dao.Certificate;
import se.inera.certificate.service.CertificateService;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith( MockitoJUnitRunner.class )
public class LakarutlatandeResourceTest {

    private static final String PERSONNUMMER = "19121212-1212";
    private static final String CERTIFICATE_ID = "123456";
    private static final String CERTIFICATE_TYPE = "<certificateType>";

    private static Certificate certificate;
    private static Utlatande utlatande;
    private static String certificateData;

    @Mock
    private CertificateService certificateService = mock(CertificateService.class);

    @Mock
    private ObjectMapper objectMapper = mock(ObjectMapper.class);

    @BeforeClass
    public static void createCertificate() {
        certificate = new Certificate(CERTIFICATE_ID, certificateData);
        certificate.setType(CERTIFICATE_TYPE);
    }

    @BeforeClass
    public static void setupCertificateData() throws IOException {
        certificateData = FileUtils.readFileToString(new ClassPathResource("lakarutlatande/maximalt-fk7263.json").getFile());
        utlatande = new CustomObjectMapper().readValue(certificateData, Utlatande.class);
    }

    @InjectMocks
    private LakarutlatandeResource resource = new LakarutlatandeResource();

    @Test
    public void testGetCertificate() throws IOException {
        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(certificate);
        when(objectMapper.readValue(certificateData, Utlatande.class)).thenReturn(utlatande);
        Response response = resource.getCertificate(PERSONNUMMER, CERTIFICATE_ID);

        verify(certificateService).getCertificate(PERSONNUMMER, CERTIFICATE_ID);
        verify(objectMapper).readValue(certificateData, Utlatande.class);

        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(utlatande, response.getEntity());
    }

    @Test
    public void testGetCertificateWithMissingConsent() {
        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenThrow(new MissingConsentException(PERSONNUMMER));
        Response response = resource.getCertificate(PERSONNUMMER, CERTIFICATE_ID);

        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    public void testGetCertificateForUnknownCertificate() {
        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(null);
        Response response = resource.getCertificate(PERSONNUMMER, CERTIFICATE_ID);

        assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    public void sendWithCorrectUserCertificateAndTarget() {
        Response response = resource.sendCertificate("userid", "certificateid", "target");
        assertEquals(200, response.getStatus());
        assertEquals("{\"resultCode\": \"sent\"}", response.getEntity());
    }

    @Test
    public void sendWithErrorGettingCertificateGeneratesError() {
        Mockito.doThrow(IllegalArgumentException.class).when(certificateService).sendCertificate(anyString(), anyString(), anyString());
        Response response = resource.sendCertificate("userid", "inknowncertificate", "target");
        assertEquals(200, response.getStatus());
        assertEquals("{\"resultCode\": \"error\"}", response.getEntity());
    }

    @Test
    public void sendWithErrorCommunicatingWithTargetFK() {
        Mockito.doThrow(IllegalArgumentException.class).when(certificateService).sendCertificate(anyString(), anyString(), anyString());
        Response response = resource.sendCertificate("userid", "certificateid", "target");
        assertEquals(200, response.getStatus());
        assertEquals("{\"resultCode\": \"error\"}", response.getEntity());
    }

}
