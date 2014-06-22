package se.inera.certificate.web.controller.api;

import static se.inera.certificate.modules.support.ApplicationOrigin.MINA_INTYG;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.api.CertificateMeta;
import se.inera.certificate.api.ConsentResponse;
import se.inera.certificate.integration.module.ModuleApiFactory;
import se.inera.certificate.modules.support.ModuleEntryPoint;
import se.inera.certificate.web.controller.api.dto.IntygModule;
import se.inera.certificate.web.security.BrowserClosedInterceptor;
import se.inera.certificate.web.security.Citizen;
import se.inera.certificate.web.service.CertificateService;
import se.inera.certificate.web.service.CitizenService;
import se.inera.certificate.web.service.ConsentService;
import se.inera.certificate.web.util.CertificateMetaConverter;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;

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
    private ModuleApiFactory moduleApiFactory;

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
        return CertificateMetaConverter.toCertificateMeta(certificateService.getCertificates(citizen.getUsername()));
    }

    @PUT
    @Path("/{id}/archive")
    @Produces(JSON_UTF8)
    public CertificateMeta archive(@PathParam("id") final String id) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'archive' for certificate {0}", id);
        return CertificateMetaConverter.toCertificateMeta(certificateService.setCertificateStatus(citizen.getUsername(), id, new LocalDateTime(),
                "MI", StatusType.DELETED));
    }

    @PUT
    @Path("/{id}/restore")
    @Produces(JSON_UTF8)
    public CertificateMeta restore(@PathParam("id") final String id) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'restore' for certificate {0}", id);
        return CertificateMetaConverter.toCertificateMeta(certificateService.setCertificateStatus(citizen.getUsername(), id, new LocalDateTime(),
                "MI", StatusType.RESTORED));
    }

    @PUT
    @Path("/{id}/send")
    public CertificateMeta send(@PathParam("id") final String id) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'send' for certificate {0}", id);
        // TODO: no hardcoding of targets
        return CertificateMetaConverter.toCertificateMeta(certificateService.setCertificateStatus(citizen.getUsername(), id, new LocalDateTime(),
                "FK", StatusType.SENT));
    }

    @POST
    @Path("/consent/give")
    public ConsentResponse giveConsent() {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'giveConsent' for citizen {0}", citizen.getUsername());
        citizen.setConsent(consentService.setConsent(citizen.getUsername(), true));
        return new ConsentResponse(true);
    }

    @POST
    @Path("/consent/revoke")
    public ConsentResponse revokeConsent() {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'revokeConsent' for citizen {0}", citizen.getUsername());
        boolean revokedSuccessfully = consentService.setConsent(citizen.getUsername(), false);
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
        List<IntygModule> response = new ArrayList<>();
        for (ModuleEntryPoint module : moduleApiFactory.getRegisteredModules()) {
            response.add(new IntygModule(module.getModuleId(), module.getModuleName(), module.getModuleDescription(),
                    module.getModuleCssPath(MINA_INTYG), module.getModuleScriptPath(MINA_INTYG)));
        }

        return response;
    }
    
    /**
     * Endpoint used by client to notify server that onbeforeunload is triggered
     * 
     * @param req javax.servlet.http.HttpServletRequest
     * @return
     */
    @GET
    @Path("/onbeforeunload")
    public String onbeforeunload(@Context HttpServletRequest req) {
        if (req.getSession().getAttribute(BrowserClosedInterceptor.BROWSER_CLOSED_TIMESTAMP) == null) {
            req.getSession().setAttribute(BrowserClosedInterceptor.BROWSER_CLOSED_TIMESTAMP, DateTime.now());
        }
        else {
            LOG.warn("onbeforeonload already set");
        }
        return "ok";
    }
}
