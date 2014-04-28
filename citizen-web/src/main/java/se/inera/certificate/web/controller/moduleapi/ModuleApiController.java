/**
 * Copyright (C) 2012 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Web (http://code.google.com/p/inera-certificate-web).
 *
 * Inera Certificate Web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.web.controller.moduleapi;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

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

import se.inera.certificate.api.ModuleAPIResponse;
import se.inera.certificate.integration.exception.ExternalWebServiceCallFailedException;
import se.inera.certificate.integration.module.ModuleApiFactory;
import se.inera.certificate.integration.module.dto.CertificateContentHolder;
import se.inera.certificate.integration.module.exception.ModuleNotFoundException;
import se.inera.certificate.modules.support.ModuleEntryPoint;
import se.inera.certificate.modules.support.api.dto.ExternalModelHolder;
import se.inera.certificate.modules.support.api.dto.InternalModelResponse;
import se.inera.certificate.modules.support.api.dto.PdfResponse;
import se.inera.certificate.modules.support.api.exception.ModuleException;
import se.inera.certificate.web.security.Citizen;
import se.inera.certificate.web.service.CertificateService;
import se.inera.certificate.web.service.CitizenService;

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
    private ModuleApiFactory moduleApiFactory;

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

        CertificateContentHolder contentHolder = certificateService.getUtlatande(citizenService.getCitizen().getUsername(), id);

        LOG.debug("getCertificate - type is: {}", contentHolder.getCertificateContentMeta().getType());

        try {
            ModuleEntryPoint module = moduleApiFactory.getModuleEntryPoint(contentHolder.getCertificateContentMeta().getType());
            InternalModelResponse response = module.getModuleApi().convertExternalToInternal(
                    new ExternalModelHolder(contentHolder.getCertificateContent()));

            return Response.ok(response.getInternalModel()).build();

        } catch (ModuleNotFoundException e) {
            LOG.error("Module " + id + " not found. Not loaded in application. Error was: " + e.getMessage());
            return Response.serverError().build();

        } catch (ModuleException e) {
            LOG.error("Failed to get module-internal representation of certificate " + id + " from module '"
                    + contentHolder.getCertificateContentMeta().getType() + "'.");
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
        ModuleAPIResponse response = certificateService.sendCertificate(citizen.getUsername(), id, target);
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

        CertificateContentHolder certificateContentHolder;

        try {
            certificateContentHolder = certificateService.getUtlatande(citizenService.getCitizen().getUsername(), id);
        } catch (ExternalWebServiceCallFailedException ex) {
            return Response.status(INTERNAL_SERVER_ERROR).build();
        }

        try {
            ModuleEntryPoint module = moduleApiFactory.getModuleEntryPoint(certificateContentHolder.getCertificateContentMeta().getType());
            PdfResponse pdf = module.getModuleApi().pdf(
                    new ExternalModelHolder(certificateContentHolder.getCertificateContent()));
            String filename = pdf.getFilename();
            return Response.ok(pdf.getPdfData())
                    .header(CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .build();

        } catch (ModuleNotFoundException e) {
            LOG.error("Module " + id + " not found. Not loaded in application.");
            return Response.serverError().build();
        } catch (ModuleException e) {
            LOG.error("Failed to get PDF for certificate " + id + " from inera-certificate.");
            return Response.serverError().build();
        }
    }
}
