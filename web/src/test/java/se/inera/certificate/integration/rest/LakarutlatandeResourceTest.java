package se.inera.certificate.integration.rest;

import javax.ws.rs.core.Response;
import java.io.IOException;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NOT_IMPLEMENTED;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.service.CertificateService;

@RunWith( MockitoJUnitRunner.class )
public class LakarutlatandeResourceTest {

    private static final String PERSONNUMMER = "19121212-1212";
    private static final String CERTIFICATE_ID = "123456";
    private static final String CERTIFICATE_TYPE = "<certificateType>";

    private static Certificate certificate;
    private static Lakarutlatande lakarutlatande;
    private static String certificateData;

    @Mock
    private CertificateService certificateService = mock(CertificateService.class);

    @Mock
    private ModuleRestApiFactory moduleRestApiFactory = mock(ModuleRestApiFactory.class);

    @Mock
    private ObjectMapper objectMapper = mock(ObjectMapper.class);

    private ModuleRestApi moduleRestApi = mock(ModuleRestApi.class);


    @BeforeClass
    public static void createCertificate() {
        certificate = new Certificate(CERTIFICATE_ID, certificateData);
        certificate.setType(CERTIFICATE_TYPE);
    }

    @BeforeClass
    public static void setupCertificateData() throws IOException {
        certificateData = FileUtils.readFileToString(new ClassPathResource("lakarutlatande/maximalt-fk7263.json").getFile());
        lakarutlatande = new CustomObjectMapper().readValue(certificateData, Lakarutlatande.class);
    }

    @InjectMocks
    LakarutlatandeResource resource = new LakarutlatandeResource();

    @Test
    public void testGetCertificate() throws IOException {
        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(certificate);
        when(objectMapper.readValue(certificateData, Lakarutlatande.class)).thenReturn(lakarutlatande);
        Response response = resource.getCertificate(PERSONNUMMER, CERTIFICATE_ID);

        verify(certificateService).getCertificate(PERSONNUMMER, CERTIFICATE_ID);
        verify(objectMapper).readValue(certificateData, Lakarutlatande.class);

        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(lakarutlatande, response.getEntity());
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
    public void testGetCertificatePdf() throws IOException {

        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(certificate);
        when(moduleRestApiFactory.getModuleRestService(CERTIFICATE_TYPE)).thenReturn(moduleRestApi);
        when(objectMapper.readValue(certificateData, Lakarutlatande.class)).thenReturn(lakarutlatande);

        // Mimic the module API to which the PDF generation is delegated to.
        // We return an HTTP 200 together with some mock PDF data.
        Response moduleCallResponse = mock(Response.class);
        when(moduleCallResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(moduleCallResponse.getEntity()).thenReturn("<pdf-file>");
        when(moduleRestApi.pdf(lakarutlatande)).thenReturn(moduleCallResponse);

        Response response = resource.getCertificatePdf(PERSONNUMMER, CERTIFICATE_ID);

        verify(certificateService).getCertificate(PERSONNUMMER, CERTIFICATE_ID);
        verify(moduleRestApiFactory).getModuleRestService(CERTIFICATE_TYPE);
        verify(objectMapper).readValue(certificateData, Lakarutlatande.class);
        verify(moduleRestApi).pdf(lakarutlatande);

        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals("<pdf-file>", response.getEntity());
    }

    @Test
    public void testGetCertificatePdfWithFailingModule() throws IOException {

        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(certificate);
        when(moduleRestApiFactory.getModuleRestService(CERTIFICATE_TYPE)).thenReturn(moduleRestApi);
        when(objectMapper.readValue(certificateData, Lakarutlatande.class)).thenReturn(lakarutlatande);

        // Mimic the module API to which the PDF generation is delegated to.
        // We return an HTTP 501.
        Response moduleCallResponse = mock(Response.class);
        when(moduleCallResponse.getStatus()).thenReturn(Response.Status.NOT_IMPLEMENTED.getStatusCode());
        when(moduleRestApi.pdf(lakarutlatande)).thenReturn(moduleCallResponse);

        Response response = resource.getCertificatePdf(PERSONNUMMER, CERTIFICATE_ID);

        verify(certificateService).getCertificate(PERSONNUMMER, CERTIFICATE_ID);
        verify(moduleRestApiFactory).getModuleRestService(CERTIFICATE_TYPE);
        verify(objectMapper).readValue(certificateData, Lakarutlatande.class);
        verify(moduleRestApi).pdf(lakarutlatande);

        assertEquals(NOT_IMPLEMENTED.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    public void testGetCertificatePdfWithFailingUnmarshaller() throws IOException {

        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(certificate);
        when(objectMapper.readValue(certificateData, Lakarutlatande.class)).thenThrow(new IOException());

        Response response = resource.getCertificatePdf(PERSONNUMMER, CERTIFICATE_ID);

        verify(certificateService).getCertificate(PERSONNUMMER, CERTIFICATE_ID);

        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    public void testGetCertificatePdfWithMissingConsent() {
        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenThrow(new MissingConsentException(PERSONNUMMER));
        Response response = resource.getCertificatePdf(PERSONNUMMER, CERTIFICATE_ID);

        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    public void testGetCertificatePdfForUnknownCertificate() {
        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(null);
        Response response = resource.getCertificatePdf(PERSONNUMMER, CERTIFICATE_ID);

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
