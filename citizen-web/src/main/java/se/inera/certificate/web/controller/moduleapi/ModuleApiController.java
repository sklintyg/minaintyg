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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.inera.certificate.integration.IneraCertificateRestApi;

import static javax.ws.rs.core.Response.Status.*;

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

        return Response.ok(response.readEntity(String.class)).header("Content-Disposition", "attachment; filename=intyg.pdf").build();
    }
}
