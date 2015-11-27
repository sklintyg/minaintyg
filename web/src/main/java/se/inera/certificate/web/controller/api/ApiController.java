package se.inera.certificate.web.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.api.CertificateMeta;
import se.inera.certificate.api.ConsentResponse;
import se.inera.certificate.web.security.BrowserClosedInterceptor;
import se.inera.certificate.web.security.Citizen;
import se.inera.certificate.web.service.CertificateService;
import se.inera.certificate.web.service.CitizenService;
import se.inera.certificate.web.service.ConsentService;
import se.inera.certificate.web.service.dto.UtlatandeRecipient;
import se.inera.certificate.web.util.CertificateMetaConverter;
import se.inera.intyg.common.support.modules.registry.IntygModule;
import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;

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

    @Autowired
    private IntygModuleRegistry moduleRegistry;

    @GET
    @Path("/test")
    public String test() {
        LOG.debug("api.test");
        return "test";
    }

    @GET
    @Produces(JSON_UTF8)
    public List<CertificateMeta> listCertificates() {
        Citizen citizen = citizenService.getCitizen();
        return CertificateMetaConverter.toCertificateMeta(certificateService.getCertificates(new Personnummer(citizen.getUsername())));
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
        LOG.debug("Requesting 'archive' for certificate {0}", id);
        return CertificateMetaConverter.toCertificateMeta(certificateService.archiveCertificate(id, new Personnummer(citizen.getUsername())));
    }

    @PUT
    @Path("/{id}/restore")
    @Produces(JSON_UTF8)
    public CertificateMeta restore(@PathParam("id") final String id) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'restore' for certificate {0}", id);
        return CertificateMetaConverter.toCertificateMeta(certificateService.restoreCertificate(id, new Personnummer(citizen.getUsername())));
    }

    @POST
    @Path("/consent/give")
    public ConsentResponse giveConsent() {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'giveConsent' for citizen {0}", citizen.getUsername());
        citizen.setConsent(consentService.setConsent(new Personnummer(citizen.getUsername())));
        return new ConsentResponse(true);
    }

    @POST
    @Path("/consent/revoke")
    public ConsentResponse revokeConsent() {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'revokeConsent' for citizen {0}", citizen.getUsername());
        boolean revokedSuccessfully = consentService.revokeConsent(new Personnummer(citizen.getUsername()));
        if (revokedSuccessfully) {
            citizen.setConsent(false);
        }
        return new ConsentResponse(revokedSuccessfully);
    }

    /**
     * Serving module configuration for Angular bootstrapping.
     *
     * @return a JSON object
     */
    @GET
    @Path("/map")
    @Produces(JSON_UTF8)
    public List<IntygModule> getModulesMap() {
        return moduleRegistry.listAllModules();
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
            req.getSession().setAttribute(BrowserClosedInterceptor.BROWSER_CLOSED_TIMESTAMP, DateTime.now());
        } else {
            LOG.warn("onbeforeonload already set");
        }
        return "ok";
    }
}
