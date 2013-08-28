package se.inera.certificate.web.controller.moduleapi;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
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

import se.inera.certificate.integration.exception.ExternalWebServiceCallFailedException;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.integration.rest.ModuleRestApi;
import se.inera.certificate.integration.rest.ModuleRestApiFactory;
import se.inera.certificate.integration.rest.dto.CertificateContentHolder;
import se.inera.certificate.integration.rest.dto.CertificateContentMeta;
import se.inera.certificate.model.Utlatande;
import se.inera.certificate.web.security.Citizen;
import se.inera.certificate.web.service.CertificateService;
import se.inera.certificate.web.service.CitizenService;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class ModuleApiControllerTest {

    private static final String PERSONNUMMER = "19121212-1212";
    private static final String CERTIFICATE_ID = "123456";
    private static final String CERTIFICATE_TYPE = "fk7263";

    private static CertificateContentHolder utlatandeHolder;
    private static String certificateData;

    @Mock
    private CertificateService certificateService = mock(CertificateService.class);

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
        utlatandeHolder = new CertificateContentHolder();
        utlatandeHolder.setCertificateContent(certificateData);

        Utlatande commonUtlatande = new CustomObjectMapper().readValue(certificateData, Utlatande.class);
        CertificateContentMeta meta = new CertificateContentMeta();
        meta.setId(commonUtlatande.getId().getExtension());
        meta.setType(commonUtlatande.getTyp().getCode().toLowerCase());
        meta.setPatientId(commonUtlatande.getPatient().getId().getExtension());
        meta.setFromDate(commonUtlatande.getValidFromDate());
        meta.setTomDate(commonUtlatande.getValidToDate());
        utlatandeHolder.setCertificateContentMeta(meta);
    }

    @Test
    public void testGetCertificatePdf() throws IOException {

        when(certificateService.getUtlatande(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(utlatandeHolder);
        when(moduleRestApiFactory.getModuleRestService(CERTIFICATE_TYPE)).thenReturn(moduleRestApi);

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        // Mimic the module API to which the PDF generation is delegated to.
        // We return an HTTP 200 together with some mock PDF data.
        Response moduleCallResponse = mock(Response.class);
        when(moduleCallResponse.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(moduleCallResponse.getEntity()).thenReturn("<pdf-file>");
        when(moduleRestApi.pdf(utlatandeHolder)).thenReturn(moduleCallResponse);

        Response response = moduleApiController.getCertificatePdf(CERTIFICATE_ID);

        verify(certificateService).getUtlatande(PERSONNUMMER, CERTIFICATE_ID);
        verify(moduleRestApiFactory).getModuleRestService(CERTIFICATE_TYPE);
        verify(moduleRestApi).pdf(utlatandeHolder);

        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals("<pdf-file>", response.getEntity());
    }

    private Citizen mockCitizen() {
        Citizen citizen = mock(Citizen.class);
        when(citizen.getUsername()).thenReturn(PERSONNUMMER);
        return citizen;
    }

    @Test
    public void testGetCertificatePdfWithFailingModule() throws IOException {
        when(certificateService.getUtlatande(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(utlatandeHolder);
        when(moduleRestApiFactory.getModuleRestService(CERTIFICATE_TYPE)).thenReturn(moduleRestApi);

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        // Mimic the module API to which the PDF generation is delegated to.
        // We return an HTTP 501.
        Response moduleCallResponse = mock(Response.class);
        when(moduleCallResponse.getStatus()).thenReturn(Response.Status.NOT_IMPLEMENTED.getStatusCode());
        when(moduleRestApi.pdf(utlatandeHolder)).thenReturn(moduleCallResponse);

        Response response = moduleApiController.getCertificatePdf(CERTIFICATE_ID);

        verify(certificateService).getUtlatande(PERSONNUMMER, CERTIFICATE_ID);
        verify(moduleRestApiFactory).getModuleRestService(CERTIFICATE_TYPE);
        verify(moduleRestApi).pdf(utlatandeHolder);

        assertEquals(NOT_IMPLEMENTED.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    public void testGetCertificatePdfWithFailingIntygstjanst() {
        Response certificateResponse = mock(Response.class);
        when(certificateResponse.getStatus()).thenReturn(Response.Status.FORBIDDEN.getStatusCode());
        when(certificateService.getUtlatande(PERSONNUMMER, CERTIFICATE_ID)).thenThrow(ExternalWebServiceCallFailedException.class);

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        Response response = moduleApiController.getCertificatePdf(CERTIFICATE_ID);

        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

}
