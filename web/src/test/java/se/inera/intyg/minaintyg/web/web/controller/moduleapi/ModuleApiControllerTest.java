package se.inera.intyg.minaintyg.web.web.controller.moduleapi;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.InternalModelHolder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;
import se.inera.intyg.minaintyg.web.exception.ExternalWebServiceCallFailedException;
import se.inera.intyg.minaintyg.web.web.security.Citizen;
import se.inera.intyg.minaintyg.web.web.service.CertificateService;
import se.inera.intyg.minaintyg.web.web.service.CitizenService;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeWithMeta;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class ModuleApiControllerTest {

    private static final Personnummer PERSONNUMMER = new Personnummer("19121212-1212");
    private static final String CERTIFICATE_ID = "123456";
    private static final String CERTIFICATE_TYPE = "fk7263";

    private static UtlatandeWithMeta utlatandeHolder;
    private static InternalModelHolder internalModelHolder;
    private static String certificateData;

    @Mock
    private CertificateService certificateService = mock(CertificateService.class);

    @Mock
    private IntygModuleRegistry moduleRegistry;

    @Mock
    private ModuleApi moduleApi;

    @Mock
    private CitizenService citizenService;

    @Mock
    private ObjectMapper objectMapper = mock(ObjectMapper.class);

    @InjectMocks
    private ModuleApiController moduleApiController = new ModuleApiController();

    @BeforeClass
    public static void setupCertificateData() throws IOException {
        certificateData = FileUtils.readFileToString(new ClassPathResource("lakarutlatande/maximalt-fk7263.json").getFile());

        Utlatande utlatande = new CustomObjectMapper().readValue(certificateData, Utlatande.class);
        List<Status> status = new ArrayList<Status>();
        status.add(new Status(CertificateState.SENT, "FK", LocalDateTime.now()));
        utlatandeHolder = new UtlatandeWithMeta(utlatande, certificateData, status);

        internalModelHolder = new InternalModelHolder(certificateData);
    }

    @Test
    public void testGetCertificatePdf() throws Exception {
        when(certificateService.getUtlatande(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(utlatandeHolder);
        when(moduleRegistry.getModuleApi(CERTIFICATE_TYPE)).thenReturn(moduleApi);

        byte[] bytes = "<pdf-file>".getBytes();
        when(moduleApi.pdf(refEq(internalModelHolder), any(List.class), refEq(ApplicationOrigin.MINA_INTYG))).thenReturn(
                new PdfResponse(bytes, "pdf-filename.pdf"));

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        Response response = moduleApiController.getCertificatePdf(CERTIFICATE_ID);

        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(bytes, response.getEntity());
    }

    private Citizen mockCitizen() {
        Citizen citizen = mock(Citizen.class);
        when(citizen.getUsername()).thenReturn(PERSONNUMMER.getPersonnummer());
        return citizen;
    }

    @Test
    public void testGetCertificatePdfWithFailingModule() throws Exception {
        when(certificateService.getUtlatande(PERSONNUMMER, CERTIFICATE_ID)).thenReturn(utlatandeHolder);
        when(moduleRegistry.getModuleApi(CERTIFICATE_TYPE)).thenReturn(moduleApi);
        when(moduleApi.pdf(refEq(internalModelHolder), any(List.class), refEq(ApplicationOrigin.MINA_INTYG))).thenThrow(
                new ModuleSystemException());

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        Response response = moduleApiController.getCertificatePdf(CERTIFICATE_ID);

        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
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
