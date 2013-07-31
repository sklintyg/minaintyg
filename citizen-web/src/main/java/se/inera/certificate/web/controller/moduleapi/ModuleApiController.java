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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.certificate.api.ModuleAPIResponse;
import se.inera.certificate.integration.IneraCertificateRestApi;
import se.inera.certificate.integration.rest.ModuleRestApi;
import se.inera.certificate.integration.rest.ModuleRestApiFactory;
import se.inera.certificate.model.Utlatande;
import se.inera.certificate.web.security.Citizen;
import se.inera.certificate.web.service.CertificateService;
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
    private static final String DATE_FORMAT = "yyyyMMdd";

    /**
     * Intygstjanstens REST endpoint service.
     */
    @Autowired
    private IneraCertificateRestApi certificateRestService;

    @Autowired
    private ModuleRestApiFactory moduleApiFactory;
    
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

    @Autowired
    private JacksonJaxbJsonProvider jsonProvider;

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
        if (isNotOk(response)) {
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
     * @param id
     *            - the globally unique id of a certificate.
     * @return The certificate in PDF format
     */
    @GET
    @Path("/{id}/pdf")
    @Produces("application/pdf")
    public final Response getCertificatePdf(@PathParam(value = "id") final String id) {
        LOG.debug("getCertificatePdf: {}", id);

        Response response = certificateRestService.getCertificate(citizenService.getCitizen().getUsername(), id);
        if (isNotOk(response)) {
            LOG.error("Failed to get certificate " + id + " from inera-certificate.");
            return Response.status(response.getStatus()).build();
        }

        Utlatande utlatande = response.readEntity(Utlatande.class);

        Response pdf = fetchPdf(utlatande);

        if (isNotOk(pdf)) {
            LOG.error("Failed to get PDF for certificate " + id + " from inera-certificate.");
            return Response.status(pdf.getStatus()).build();
        }
        
        return Response.ok(pdf.getEntity()).header(CONTENT_DISPOSITION, "attachment; filename=" + pdfFileName(utlatande)).build();
    }

    private boolean isNotOk(Response response) {
        return response.getStatus() != OK.getStatusCode();
    }

    private Response fetchPdf(Utlatande utlatande) {
        ModuleRestApi api = moduleApiFactory.getModuleRestService(utlatande.getTyp().getCode());
        Response pdf = api.pdf(utlatande);
        return pdf;
    }

    private String pdfFileName(Utlatande utlatande) {
        return String.format("lakarutlatande_%s_%s-%s.pdf",
                utlatande.getPatient().getId().getExtension(),
                utlatande.getValidFromDate().toString(DATE_FORMAT),
                utlatande.getValidToDate().toString(DATE_FORMAT));
    }

}
