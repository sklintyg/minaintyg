/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.common.support.modules.registry.ModuleNotFoundException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.InternalModelHolder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.minaintyg.web.api.Certificate;
import se.inera.intyg.minaintyg.web.api.CertificateStatus;
import se.inera.intyg.minaintyg.web.api.ModuleAPIResponse;
import se.inera.intyg.minaintyg.web.exception.ExternalWebServiceCallFailedException;
import se.inera.intyg.minaintyg.web.web.security.Citizen;
import se.inera.intyg.minaintyg.web.web.service.CertificateService;
import se.inera.intyg.minaintyg.web.web.service.CitizenService;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeWithMeta;
import se.inera.intyg.minaintyg.web.web.util.CertificateStatusConverter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Controller that exposes a REST interface to functions common to certificate modules, such as get and send
 * certificate.
 *
 * @author marced
 */
public class ModuleApiController {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ModuleApiController.class);
    private static final String CONTENT_DISPOSITION = "Content-Disposition";

    @Autowired
    private IntygModuleRegistry moduleRegistry;

    /**
     * Helper service to get current user.
     */
    @Autowired
    private CitizenService citizenService;

    /**
     * Intygstjanstens WS endpoint service.
     */
    @Autowired
    private CertificateService certificateService;

    private ObjectMapper objectMapper = new CustomObjectMapper();

    /**
     * Return the certificate identified by the given id as JSON.
     *
     * @param id
     *            - the globally unique id of a certificate.
     * @return The certificate in JSON format
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final Response getCertificate(@PathParam("id") final String id) {
        LOG.debug("getCertificate: {}", id);

        UtlatandeWithMeta utlatande = certificateService.getUtlatande(new Personnummer(citizenService.getCitizen().getUsername()), id);

        try {
            JsonNode utlatandeJson = objectMapper.readTree(utlatande.getDocument());
            CertificateStatus meta = CertificateStatusConverter.toCertificateStatus(utlatande.getStatuses());
            return Response.ok(new Certificate(utlatandeJson, meta)).build();

        } catch (IOException e) {
            LOG.error("Failed to serialize module-internal representation of certificate " + id);
            return Response.serverError().build();
        }
    }

    /**
     * Send the certificate identified by the given id to the given target.
     *
     * @param id
     *            - the globally unique id of a certificate.
     * @param target
     *            - id of target system that should receive the certificate.
     * @return The response of the send operation
     */
    @PUT
    @Path("/{id}/send/{target}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response send(@PathParam("id") final String id, @PathParam("target") final String target) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'send' for certificate {} to target {}", id, target);
        ModuleAPIResponse response = certificateService.sendCertificate(new Personnummer(citizen.getUsername()), id, target);
        return Response.ok(response).build();
    }

    /**
     * Return the certificate identified by the given id as PDF.
     *
     * @param id
     *            - the globally unique id of a certificate.
     * @return The certificate in PDF format
     */
    @GET
    @Path("/{id}/pdf")
    @Produces("application/pdf")
    public final Response getCertificatePdf(@PathParam(value = "id") final String id) {
        LOG.debug("getCertificatePdf: {}", id);

        UtlatandeWithMeta utlatande;

        try {
            utlatande = certificateService.getUtlatande(new Personnummer(citizenService.getCitizen().getUsername()), id);
        } catch (ExternalWebServiceCallFailedException ex) {
            return Response.status(INTERNAL_SERVER_ERROR).build();
        }

        String typ = utlatande.getUtlatande().getTyp();

        try {
            PdfResponse pdf = moduleRegistry.getModuleApi(typ).pdf(new InternalModelHolder(utlatande.getDocument()), utlatande.getStatuses(),
                    ApplicationOrigin.MINA_INTYG);
            String filename = pdf.getFilename();
            return Response.ok(pdf.getPdfData())
                    .header(CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .build();

        } catch (ModuleNotFoundException e) {
            LOG.error("Module " + typ + " not found. Not loaded in application.");
            return Response.serverError().build();
        } catch (ModuleException e) {
            LOG.error("Failed to get PDF for certificate " + id + " from inera-certificate.");
            return Response.serverError().build();
        }
    }
}
