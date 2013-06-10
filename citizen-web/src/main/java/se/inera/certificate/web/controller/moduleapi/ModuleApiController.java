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

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.api.CertificateMeta;
import se.inera.certificate.integration.IneraCertificateRestApi;
import se.inera.certificate.web.security.Citizen;
import se.inera.certificate.web.service.CertificateService;
import se.inera.certificate.web.service.CitizenService;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;

/**
 * Controller that exposes a REST interface to functions common to certificate modules, such as get and send certificate.
 *
 * @author marced
 */
public class ModuleApiController {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ModuleApiController.class);

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
     * Intygstjanstens WS endpoint service
     */
    @Autowired
    private CertificateService certificateService;
    
    /**
     * Return the certificate identified by the given id as JSON.
     *
     * @param id - the globally unique id of a certificate.
     * @return The certificate in JSON format
     */
    @GET
    @Path( "/{id}" )
    @Produces( MediaType.APPLICATION_JSON + ";charset=utf-8" )
    public final Response getCertificate(@PathParam( "id" ) final String id) {
        LOG.debug("getCertificate: {}", id);

        Response response = certificateRestService.getCertificate(id);
        if (response.getStatus() != OK.getStatusCode()) {
            LOG.error("Failed to get JSON for certificate " + id + " from inera-certificate.");
            return Response.status(response.getStatus()).build();
        }

        return Response.ok(response.readEntity(String.class)).build();
    }
    
    @PUT
    @Path("/{id}/send/{target}")
    public CertificateMeta send(@PathParam("id") final String id, @PathParam("target") final String target) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'send' for certificate {0} to target {1}", id, target);
        return certificateService.setCertificateStatus(citizen.getUsername(), id, new LocalDateTime(), target, StatusType.SENT);
    }

    /**
     * Return the certificate identified by the given id as PDF.
     *
     * @param id - the globally unique id of a certificate.
     * @return The certificate in JSON format
     */
    @GET
    @Path( "/{id}/pdf" )
    @Produces( "application/pdf" )
    public final Response getCertificatePdf(@PathParam( value = "id" ) final String id) {
        LOG.debug("getCertificatePdf: {}", id);

        Response response = certificateRestService.getCertificatePdf(id);
        if (response.getStatus() != OK.getStatusCode()) {
            LOG.error("Failed to get PDF for certificate " + id + " from inera-certificate.");
            return Response.status(response.getStatus()).build();
        }

        return Response.ok(response.getEntity()).header("Content-Disposition", "attachment; filename=intyg.pdf").build();
    }
}
