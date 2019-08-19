/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.minaintyg.web.controller.moduleapi;

import static javax.ws.rs.core.Response.Status.GONE;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.core.Response;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.inera.intyg.minaintyg.web.security.Citizen;
import se.inera.intyg.minaintyg.web.service.CertificateService;
import se.inera.intyg.minaintyg.web.service.CitizenService;
import se.inera.intyg.schemas.contract.Personnummer;

public abstract class ModuleApiControllerTest {

    static String certificateData;
    static CertificateResponse utlatandeHolder;
    static CertificateResponse revokedUtlatandeHolder;

    private String personnummer;
    private String certificateId;
    private String certificateType;
    private String certificateTypeVersion;

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

    public void setPersonnummer(String personnummer) {
        this.personnummer = personnummer;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public void setCertificateTypeVersion(String certificateTypeVersion) {
        this.certificateTypeVersion = certificateTypeVersion;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetCertificate() throws Exception {
        when(certificateService.getUtlatande(certificateType, certificateTypeVersion, createPnr(), certificateId))
            .thenReturn(Optional.of(utlatandeHolder));

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        Response response = moduleApiController.getCertificate(certificateType, certificateTypeVersion, certificateId);

        assertEquals(OK.getStatusCode(), response.getStatus());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetRevokedCertificate() throws Exception {
        when(certificateService.getUtlatande(certificateType, certificateTypeVersion, createPnr(), certificateId))
            .thenReturn(Optional.of(revokedUtlatandeHolder));

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        Response response = moduleApiController.getCertificate(certificateType, certificateTypeVersion, certificateId);

        assertEquals(GONE.getStatusCode(), response.getStatus());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetCertificatePdf() throws Exception {
        when(certificateService.getUtlatande(certificateType, certificateTypeVersion, createPnr(), certificateId))
            .thenReturn(Optional.of(utlatandeHolder));
        when(moduleRegistry.getModuleApi(certificateType, certificateTypeVersion)).thenReturn(moduleApi);

        byte[] bytes = "<pdf-file>".getBytes();
        when(moduleApi.pdf(eq(certificateData), any(List.class), refEq(ApplicationOrigin.MINA_INTYG), eq(UtkastStatus.SIGNED)))
            .thenReturn(new PdfResponse(bytes, "pdf-filename.pdf"));

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        Response response = moduleApiController.getCertificatePdf(certificateType, certificateTypeVersion, certificateId);

        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(bytes, response.getEntity());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetCertificatePdfRevokedCertificate() throws Exception {
        when(certificateService.getUtlatande(certificateType, certificateTypeVersion, createPnr(), certificateId))
            .thenReturn(Optional.of(revokedUtlatandeHolder));

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        Response response = moduleApiController.getCertificatePdf(certificateType, certificateTypeVersion, certificateId);

        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetCertificateEmployerPdf() throws Exception {
        when(certificateService.getUtlatande(certificateType, certificateTypeVersion, createPnr(), certificateId))
            .thenReturn(Optional.of(utlatandeHolder));
        when(moduleRegistry.getModuleApi(certificateType, certificateTypeVersion)).thenReturn(moduleApi);

        byte[] bytes = "<pdf-file>".getBytes();
        when(moduleApi.pdfEmployer(eq(certificateData), any(List.class), refEq(ApplicationOrigin.MINA_INTYG), any(List.class),
            eq(UtkastStatus.SIGNED)))
            .thenReturn(new PdfResponse(bytes, "pdf-filename.pdf"));

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        Response response = moduleApiController
            .getCertificatePdfEmployerCopy(certificateType, certificateTypeVersion, certificateId, new ArrayList<>());

        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(bytes, response.getEntity());
    }


    @SuppressWarnings("unchecked")
    @Test
    public void testGetCertificatePdfWithFailingModule() throws Exception {
        when(certificateService.getUtlatande(certificateType, certificateTypeVersion, createPnr(), certificateId))
            .thenReturn(Optional.of(utlatandeHolder));
        when(moduleRegistry.getModuleApi(certificateType, certificateTypeVersion)).thenReturn(moduleApi);
        when(moduleApi.pdf(eq(certificateData), any(List.class), refEq(ApplicationOrigin.MINA_INTYG), eq(UtkastStatus.SIGNED)))
            .thenThrow(new ModuleSystemException("error"));

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        Response response = moduleApiController.getCertificatePdf(certificateType, certificateTypeVersion, certificateId);

        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    public void testGetCertificatePdfWithFailingIntygstjanst() {
        when(certificateService.getUtlatande(certificateType, certificateTypeVersion, createPnr(), certificateId))
            .thenReturn(Optional.empty());

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        Response response = moduleApiController.getCertificatePdf(certificateType, certificateTypeVersion, certificateId);

        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

    private Citizen mockCitizen() {
        Citizen citizen = mock(Citizen.class);
        when(citizen.getUsername()).thenReturn(createPnr().getPersonnummer());
        return citizen;
    }

    private Personnummer createPnr() {
        return Personnummer.createPersonnummer(personnummer)
            .orElseThrow(() -> new IllegalArgumentException("Could not parse passed personnummer: " + personnummer));
    }

}
