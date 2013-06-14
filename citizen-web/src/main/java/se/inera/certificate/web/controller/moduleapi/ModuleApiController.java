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

import static javax.ws.rs.core.Response.Status.OK;

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

import se.inera.certificate.api.ModuleAPIRestReponse;
import se.inera.certificate.integration.IneraCertificateRestApi;
import se.inera.certificate.web.security.Citizen;
import se.inera.certificate.web.service.CitizenService;

/**
 * Controller that exposes a REST interface to functions common to certificate modules, such as get and send certificate.
 * @author marced
 */
public class ModuleApiController {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ModuleApiController.class);
    private static final String CONTENT_DISPOSITION = "Content-Disposition";

    /**
     * Intygstjanstens REST endpoint service.
     */
    @Autowired
    private IneraCertificateRestApi certificateRestService;

    /**
     * Helper service to get current user.
     */
    @Autowired
    private CitizenService citizenService;

    /**
     * Return the certificate identified by the given id as JSON.
     * @param id
     *            - the globally unique id of a certificate.
     * @return The certificate in JSON format
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public final Response getCertificate(@PathParam("id") final String id) {
        LOG.debug("getCertificate: {}", id);

        Response response = certificateRestService.getCertificate(citizenService.getCitizen().getUsername(), id);
        if (response.getStatus() != OK.getStatusCode()) {
            LOG.error("Failed to get JSON for certificate " + id + " from inera-certificate.");
            return Response.status(response.getStatus()).build();
        }

        return Response.ok(response.readEntity(String.class)).build();
    }

    /**
     * Send the certificate identified by the given id to the given target.
     * @param id
     *            - the globally unique id of a certificate.
     * @param target
     *            - id of target system that should receive the certificate.
     * @return The certificate in JSON format
     */
    @PUT
    @Path("/{id}/send/{target}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response send(@PathParam("id") final String id, @PathParam("target") final String target) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'send' for certificate {} to target {}", id, target);
        Response response = certificateRestService.sendCertificate(citizen.getUsername(), id, target);
        if (response.getStatus() != OK.getStatusCode()) {
            LOG.error("Failed to send certificate, returning error result");
            return Response.ok(new ModuleAPIRestReponse("error", "GENERIC_ERROR")).build();
        } else {
            // return json reponse as-is from IT
            return Response.ok(response.readEntity(String.class)).build();

        }
    }

    /**
     * Return the certificate identified by the given id as PDF.
     * @param id
     *            - the globally unique id of a certificate.
     * @return The certificate in PDF format
     */
    @GET
    @Path("/{id}/pdf")
    @Produces("application/pdf")
    public final Response getCertificatePdf(@PathParam(value = "id") final String id) {
        LOG.debug("getCertificatePdf: {}", id);

        Response response = certificateRestService.getCertificatePdf(citizenService.getCitizen().getUsername(), id);
        if (response.getStatus() != OK.getStatusCode()) {
            LOG.error("Failed to get PDF for certificate " + id + " from inera-certificate.");
            return Response.status(response.getStatus()).build();
        }

        String contentDisposition = response.getHeaderString(CONTENT_DISPOSITION);
        return Response.ok(response.getEntity()).header(CONTENT_DISPOSITION, contentDisposition).build();
    }
}
