/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.controller.api;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.intyg.minaintyg.web.api.CertificateMeta;
import se.inera.intyg.minaintyg.web.api.SendToRecipientResult;
import se.inera.intyg.minaintyg.web.api.UserInfo;
import se.inera.intyg.minaintyg.web.security.BrowserClosedInterceptor;
import se.inera.intyg.minaintyg.web.security.Citizen;
import se.inera.intyg.minaintyg.web.service.CertificateService;
import se.inera.intyg.minaintyg.web.service.CitizenService;
import se.inera.intyg.minaintyg.web.service.dto.UtlatandeRecipient;
import se.inera.intyg.minaintyg.web.util.CertificateMetaConverter;
import se.inera.intyg.schemas.contract.Personnummer;

public class ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(ApiController.class);
    private static final String CHARSET_UTF_8 = ";charset=utf-8";
    public static final String JSON_UTF8 = MediaType.APPLICATION_JSON + CHARSET_UTF_8;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private CitizenService citizenService;

    @GET
    @Produces(JSON_UTF8)
    public List<CertificateMeta> listCertificates() {
        Citizen citizen = citizenService.getCitizen();
        return CertificateMetaConverter
            .toCertificateMetaFromUtlatandeMetaList(certificateService.getCertificates(createPnr(citizen.getUsername()), false));
    }

    @GET
    @Path("/archived")
    @Produces(JSON_UTF8)
    public List<CertificateMeta> listArchivedCertificates() {
        Citizen citizen = citizenService.getCitizen();
        return CertificateMetaConverter
            .toCertificateMetaFromUtlatandeMetaList(certificateService.getCertificates(createPnr(citizen.getUsername()), true));
    }

    @GET
    @Path("/{id}/recipients")
    @Produces(JSON_UTF8)
    public List<UtlatandeRecipient> listRecipients(@PathParam("id") final String id) {
        LOG.debug("Listing recipients for certificate-id: {}", id);
        return certificateService.getRecipientsForCertificate(id);
    }

    @PUT
    @Path("/{id}/archive")
    @Produces(JSON_UTF8)
    public CertificateMeta archive(@PathParam("id") final String id) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'archive' for certificate {}", id);
        return CertificateMetaConverter
            .toCertificateMetaFromUtlatandeMeta(certificateService.archiveCertificate(id, createPnr(citizen.getUsername())));
    }

    @PUT
    @Path("/{id}/restore")
    @Produces(JSON_UTF8)
    public CertificateMeta restore(@PathParam("id") final String id) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'restore' for certificate {}", id);
        return CertificateMetaConverter
            .toCertificateMetaFromUtlatandeMeta(certificateService.restoreCertificate(id, createPnr(citizen.getUsername())));
    }

    /**
     * Send the certificate identified by the given id to the given recipients.
     *
     * @param id - the globally unique id of a certificate.
     * @param recipients - List of recipient ids that should receive the certificate.
     * @return The response of the send operation
     */
    @PUT
    @Path("/{id}/{intygsTyp}/send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JSON_UTF8)
    public List<SendToRecipientResult> send(@PathParam("id") final String id, @PathParam("intygsTyp")
    final String intygsTyp, final List<String> recipients) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'send' for certificate {} to recipients {}", id, recipients);
        return certificateService.sendCertificate(createPnr(citizen.getUsername()), id, intygsTyp, recipients);
    }

    /**
     * Endpoint used by client to notify server that onbeforeunload is triggered.
     */
    @GET
    @Path("/onbeforeunload")
    public String onbeforeunload(@Context HttpServletRequest req) {
        if (req.getSession().getAttribute(BrowserClosedInterceptor.BROWSER_CLOSED_TIMESTAMP) == null) {
            req.getSession().setAttribute(BrowserClosedInterceptor.BROWSER_CLOSED_TIMESTAMP,
                LocalDateTime.now(ZoneId.systemDefault()));
        } else {
            LOG.warn("onbeforeonload already set");
        }
        return "ok";
    }

    @GET
    @Path("/questions/{intygsTyp}/{version}")
    public String getQuestions(@PathParam("intygsTyp") String intygsTyp, @PathParam("version") String version) {

        LOG.debug("Requesting questions for '{}' with version '{}'.", intygsTyp, version);

        String questions = certificateService.getQuestions(intygsTyp, version);

        return questions;
    }

    @GET
    @Path("/user")
    @Produces(JSON_UTF8)
    public Response getUser() {
        Citizen citizen = citizenService.getCitizen();
        if (citizen != null) {
            UserInfo userInfo = new UserInfo(citizen.getUsername(),
                citizen.getFullName(), citizen.getLoginMethod().name(),
                citizen.isSekretessmarkering());

            return Response.ok(userInfo).build();
        } else {
            LOG.info("No citizen in securityContext");
            return Response.status(Response.Status.FORBIDDEN).type("text/plain")
                .entity("No citizen in securityContext").build();
        }
    }

    private Personnummer createPnr(String personId) {
        return Personnummer.createPersonnummer(personId).orElse(null);
    }

}
