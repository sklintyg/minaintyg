/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

package se.inera.intyg.minaintyg.web.web.controller.moduleapi;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.inera.intyg.minaintyg.web.web.security.Citizen;
import se.inera.intyg.minaintyg.web.web.service.CertificateService;
import se.inera.intyg.minaintyg.web.web.service.CitizenService;

public abstract class ModuleApiControllerTest {

    static String certificateData;
    static CertificateResponse utlatandeHolder;

    private String personnummer;
    private String certificateId;
    private String certificateType;

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

    // - - - Setters - - -

    public void setPersonnummer(String personnummer) {
        this.personnummer = personnummer;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    // - - - Test cases - - -

    @SuppressWarnings("unchecked")
    @Test
    public void testGetCertificatePdf() throws Exception {
        when(certificateService.getUtlatande(certificateType, new Personnummer(personnummer), certificateId)).thenReturn(Optional.of(utlatandeHolder));
        when(moduleRegistry.getModuleApi(certificateType)).thenReturn(moduleApi);

        byte[] bytes = "<pdf-file>".getBytes();
        when(moduleApi.pdf(eq(certificateData), any(List.class), refEq(ApplicationOrigin.MINA_INTYG))).thenReturn(new PdfResponse(bytes, "pdf-filename.pdf"));

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        Response response = moduleApiController.getCertificatePdf(certificateType, certificateId);

        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(bytes, response.getEntity());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetCertificateEmployerPdf() throws Exception {
        when(certificateService.getUtlatande(certificateType, new Personnummer(personnummer), certificateId)).thenReturn(Optional.of(utlatandeHolder));
        when(moduleRegistry.getModuleApi(certificateType)).thenReturn(moduleApi);

        byte[] bytes = "<pdf-file>".getBytes();
        when(moduleApi.pdfEmployer(eq(certificateData), any(List.class), refEq(ApplicationOrigin.MINA_INTYG), any(List.class)))
                .thenReturn(new PdfResponse(bytes, "pdf-filename.pdf"));

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        Response response = moduleApiController.getCertificatePdfEmployerCopy(certificateType, certificateId, new ArrayList<>());

        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(bytes, response.getEntity());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetCertificatePdfWithFailingModule() throws Exception {
        when(certificateService.getUtlatande(certificateType, new Personnummer(personnummer), certificateId)).thenReturn(Optional.of(utlatandeHolder));
        when(moduleRegistry.getModuleApi(certificateType)).thenReturn(moduleApi);
        when(moduleApi.pdf(eq(certificateData), any(List.class), refEq(ApplicationOrigin.MINA_INTYG))).thenThrow(new ModuleSystemException("error"));

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        Response response = moduleApiController.getCertificatePdf(certificateType, certificateId);

        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    public void testGetCertificatePdfWithFailingIntygstjanst() {
        Response certificateResponse = mock(Response.class);
        when(certificateResponse.getStatus()).thenReturn(Response.Status.FORBIDDEN.getStatusCode());
        when(certificateService.getUtlatande(certificateType, new Personnummer(personnummer), certificateId)).thenReturn(Optional.empty());

        Citizen citizen = mockCitizen();
        when(citizenService.getCitizen()).thenReturn(citizen);

        Response response = moduleApiController.getCertificatePdf(certificateType, certificateId);

        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

    private Citizen mockCitizen() {
        Citizen citizen = mock(Citizen.class);
        when(citizen.getUsername()).thenReturn(new Personnummer(personnummer).getPersonnummer());
        return citizen;
    }

}
