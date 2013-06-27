package se.inera.certificate.web.controller.api;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.certificate.api.CertificateMeta;
import se.inera.certificate.api.ConsentResponse;
import se.inera.certificate.web.security.Citizen;
import se.inera.certificate.web.service.CertificateService;
import se.inera.certificate.web.service.CitizenService;
import se.inera.certificate.web.service.ConsentService;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;

public class ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private ConsentService consentService;

    @Autowired
    private CitizenService citizenService;

    @GET
    @Path("/test")
    public String test() {
        LOG.debug("api.test");
        return "test";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<CertificateMeta> listCertificates() {
        Citizen citizen = citizenService.getCitizen();
        return certificateService.getCertificates(citizen.getUsername());
    }

    @PUT
    @Path("/{id}/archive")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public CertificateMeta archive(@PathParam("id") final String id) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'archive' for certificate {0}", id);
        return certificateService.setCertificateStatus(citizen.getUsername(), id, new LocalDateTime(), "MI", StatusType.DELETED);
    }

    @PUT
    @Path("/{id}/restore")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public CertificateMeta restore(@PathParam("id") final String id) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'restore' for certificate {0}", id);
        return certificateService.setCertificateStatus(citizen.getUsername(), id, new LocalDateTime(), "MI", StatusType.RESTORED);
    }

    @PUT
    @Path("/{id}/send")
    public CertificateMeta send(@PathParam("id") final String id) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'send' for certificate {0}", id);
        // TODO: no hardcoding of targets
        return certificateService.setCertificateStatus(citizen.getUsername(), id, new LocalDateTime(), "FK", StatusType.SENT);
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
}
