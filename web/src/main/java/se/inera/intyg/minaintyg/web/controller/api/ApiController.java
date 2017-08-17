/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.minaintyg.web.api.CertificateMeta;
import se.inera.intyg.minaintyg.web.api.ConsentResponse;
import se.inera.intyg.minaintyg.web.api.SendToRecipientResult;
import se.inera.intyg.minaintyg.web.api.UserInfo;
import se.inera.intyg.minaintyg.web.security.BrowserClosedInterceptor;
import se.inera.intyg.minaintyg.web.security.Citizen;
import se.inera.intyg.minaintyg.web.service.CertificateService;
import se.inera.intyg.minaintyg.web.service.CitizenService;
import se.inera.intyg.minaintyg.web.service.ConsentService;
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
    private ConsentService consentService;

    @Autowired
    private CitizenService citizenService;

    @GET
    @Produces(JSON_UTF8)
    public List<CertificateMeta> listCertificates() {
        Citizen citizen = citizenService.getCitizen();
        return CertificateMetaConverter
                .toCertificateMeta(certificateService.getCertificates(new Personnummer(citizen.getUsername()), false));
    }

    @GET
    @Path("/archived")
    @Produces(JSON_UTF8)
    public List<CertificateMeta> listArchivedCertificates() {
        Citizen citizen = citizenService.getCitizen();
        return CertificateMetaConverter
                .toCertificateMeta(certificateService.getCertificates(new Personnummer(citizen.getUsername()), true));
    }

    @GET
    @Path("/{type}/recipients")
    @Produces(JSON_UTF8)
    public List<UtlatandeRecipient> listRecipients(@PathParam("type") final String type) {
        LOG.debug("Listing recipients for certificate type: {}", type);
        return certificateService.getRecipientsForCertificate(type);
    }

    @PUT
    @Path("/{id}/archive")
    @Produces(JSON_UTF8)
    public CertificateMeta archive(@PathParam("id") final String id) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'archive' for certificate {}", id);
        return CertificateMetaConverter
                .toCertificateMeta(certificateService.archiveCertificate(id, new Personnummer(citizen.getUsername())));
    }

    @PUT
    @Path("/{id}/restore")
    @Produces(JSON_UTF8)
    public CertificateMeta restore(@PathParam("id") final String id) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'restore' for certificate {}", id);
        return CertificateMetaConverter
                .toCertificateMeta(certificateService.restoreCertificate(id, new Personnummer(citizen.getUsername())));
    }

    /**
     * Send the certificate identified by the given id to the given recipients.
     *
     * @param id
     *            - the globally unique id of a certificate.
     * @param recipients
     *            - List of recipient ids that should receive the certificate.
     * @return The response of the send operation
     */
    @PUT
    @Path("/{id}/send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JSON_UTF8)
    public List<SendToRecipientResult> send(@PathParam("id") final String id, final List<String> recipients) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'send' for certificate {} to recipients {}", id, recipients);
        return certificateService.sendCertificate(new Personnummer(citizen.getUsername()), id, recipients);
    }

    @POST
    @Path("/consent/give")
    @Produces(JSON_UTF8)
    public ConsentResponse giveConsent() {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'giveConsent' for citizen {}", new Personnummer(citizen.getUsername()).getPnrHash());
        citizen.setConsent(consentService.setConsent(new Personnummer(citizen.getUsername())));
        return new ConsentResponse(true);
    }

    @POST
    @Path("/consent/revoke")
    @Produces(JSON_UTF8)
    public ConsentResponse revokeConsent() {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'revokeConsent' for citizen {}", new Personnummer(citizen.getUsername()).getPnrHash());
        boolean revokedSuccessfully = consentService.revokeConsent(new Personnummer(citizen.getUsername()));
        if (revokedSuccessfully) {
            citizen.setConsent(false);
        }
        return new ConsentResponse(revokedSuccessfully);
    }

    /**
     * Endpoint used by client to notify server that onbeforeunload is triggered.
     *
     * @param req
     * @return
     */
    @GET
    @Path("/onbeforeunload")
    public String onbeforeunload(@Context HttpServletRequest req) {
        if (req.getSession().getAttribute(BrowserClosedInterceptor.BROWSER_CLOSED_TIMESTAMP) == null) {
            req.getSession().setAttribute(BrowserClosedInterceptor.BROWSER_CLOSED_TIMESTAMP, LocalDateTime.now());
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
    public UserInfo getUser() {
        Citizen citizen = citizenService.getCitizen();
        if (citizen != null) {
            return new UserInfo(citizen.getUsername(), citizen.getFullName(), citizen.getLoginMethod().name(),
                    citizen.isSekretessmarkering(), citizen.hasConsent());
        } else {
            throw new IllegalStateException("No citizen in securityContext");
        }
    }

}
