package se.inera.certificate.web.controller.moduleapi;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NOT_IMPLEMENTED;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.certificate.integration.IneraCertificateRestApi;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.web.security.Citizen;
import se.inera.certificate.web.service.CertificateService;
import se.inera.certificate.web.service.CitizenService;

@RunWith(MockitoJUnitRunner.class)
public class ModuleApiControllerTest {

    private static final String PERSONNUMMER = "19121212-1212";
    private static final String CERTIFICATE_ID = "123456";
    private static final String CERTIFICATE_TYPE = "fk7263";

    private static Lakarutlatande lakarutlatande;
    private static String certificateData;

    @Mock
    private IneraCertificateRestApi certificateService = mock(IneraCertificateRestApi.class);

    @Mock
    private ModuleRestApiFactory moduleRestApiFactory;

    @Mock
    private CitizenService citizenService;

    @Mock
    private ModuleRestApi moduleRestApi;

    @Mock
    private ObjectMapper objectMapper = mock(ObjectMapper.class);

    @InjectMocks
    private ModuleApiController moduleApiController = new ModuleApiController();

    @BeforeClass
    public static void setupCertificateData() throws IOException {
        certificateData = FileUtils.readFileToString(new ClassPathResource("lakarutlatande/maximalt-fk7263.json").getFile());
        lakarutlatande = new CustomObjectMapper().readValue(certificateData, Lakarutlatande.class);
    }

    @Test
    public void testGetCertificatePdf() throws IOException {

        Response okCertificate = okCertificate();
        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(okCertificate);
        when(moduleRestApiFactory.getModuleRestService(CERTIFICATE_TYPE)).thenReturn(moduleRestApi);

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);
        
        // Mimic the module API to which the PDF generation is delegated to.
        // We return an HTTP 200 together with some mock PDF data.
        Response moduleCallResponse = mock(Response.class);
        when(moduleCallResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(moduleCallResponse.getEntity()).thenReturn("<pdf-file>");
        when(moduleRestApi.pdf(lakarutlatande)).thenReturn(moduleCallResponse);

        Response response = moduleApiController.getCertificatePdf(CERTIFICATE_ID);

        verify(certificateService).getCertificate(PERSONNUMMER, CERTIFICATE_ID);
        verify(moduleRestApiFactory).getModuleRestService(CERTIFICATE_TYPE);
        verify(moduleRestApi).pdf(lakarutlatande);

        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals("<pdf-file>", response.getEntity());
    }

    private Citizen mockCitizen() {
        Citizen citizen = mock(Citizen.class);
        when(citizen.getUsername()).thenReturn(PERSONNUMMER);
        return citizen;
    }

    private Response okCertificate() {
        Response certificateResponse = mock(Response.class);
        when(certificateResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(certificateResponse.readEntity(Lakarutlatande.class)).thenReturn(lakarutlatande);
        return certificateResponse;
    }

    @Test
    public void testGetCertificatePdfWithFailingModule() throws IOException {
        Response okCertificate = okCertificate();
        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(okCertificate);
        when(moduleRestApiFactory.getModuleRestService(CERTIFICATE_TYPE)).thenReturn(moduleRestApi);

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        // Mimic the module API to which the PDF generation is delegated to.
        // We return an HTTP 501.
        Response moduleCallResponse = mock(Response.class);
        when(moduleCallResponse.getStatus()).thenReturn(Response.Status.NOT_IMPLEMENTED.getStatusCode());
        when(moduleRestApi.pdf(lakarutlatande)).thenReturn(moduleCallResponse);

        Response response = moduleApiController.getCertificatePdf(CERTIFICATE_ID);

        verify(certificateService).getCertificate(PERSONNUMMER, CERTIFICATE_ID);
        verify(moduleRestApiFactory).getModuleRestService(CERTIFICATE_TYPE);
        verify(moduleRestApi).pdf(lakarutlatande);

        assertEquals(NOT_IMPLEMENTED.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    public void testGetCertificatePdfWithMissingConsent() {
        Response certificateResponse = mock(Response.class);
        when(certificateResponse.getStatus()).thenReturn(Response.Status.FORBIDDEN.getStatusCode());
        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(certificateResponse);

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        Response response = moduleApiController.getCertificatePdf(CERTIFICATE_ID);

        assertEquals(FORBIDDEN.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    public void testGetCertificatePdfForUnknownCertificate() {
        Response certificateResponse = mock(Response.class);
        when(certificateResponse.getStatus()).thenReturn(Response.Status.NOT_FOUND.getStatusCode());
        when(certificateService.getCertificate(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(certificateResponse);

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        Response response = moduleApiController.getCertificatePdf(CERTIFICATE_ID);

        assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }
}
