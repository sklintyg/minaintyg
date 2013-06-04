package se.inera.certificate.web.controller.api;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import se.inera.certificate.api.CertificateMeta;
import se.inera.certificate.api.ConsentResponse;
import se.inera.certificate.web.security.Citizen;
import se.inera.certificate.web.service.CertificateService;
import se.inera.certificate.web.service.CitizenService;
import se.inera.certificate.web.service.ConsentService;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;

@Controller
@RequestMapping(value = "/certificates", produces = "application/json")
public class ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private ConsentService consentService;

    @Autowired
    private CitizenService citizenService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        LOG.debug("api.test");
        return "test";
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<CertificateMeta> listCertificates() {
        Citizen citizen = citizenService.getCitizen();
        return certificateService.getCertificates(citizen.getUsername());
    }

    @RequestMapping(value = "/{id}/archive", method = RequestMethod.PUT)
    @ResponseBody
    public CertificateMeta archive(@PathVariable(value = "id") final String id) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'archive' for certificate {0}", id);
        return certificateService.setCertificateStatus(citizen.getUsername(), id, new LocalDateTime(), "MI", StatusType.DELETED);
    }

    @RequestMapping(value = "/{id}/restore", method = RequestMethod.PUT)
    @ResponseBody
    public CertificateMeta restore(@PathVariable(value = "id") final String id) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'restore' for certificate {0}", id);
        return certificateService.setCertificateStatus(citizen.getUsername(), id, new LocalDateTime(), "MI", StatusType.RESTORED);
    }

    @RequestMapping(value = "/{id}/send", method = RequestMethod.PUT)
    @ResponseBody
    public CertificateMeta send(@PathVariable(value = "id") final String id) {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'send' for certificate {0}", id);
        // TODO: no hardcoding of targets
        return certificateService.setCertificateStatus(citizen.getUsername(), id, new LocalDateTime(), "FK", StatusType.SENT);
    }

    @RequestMapping(value = "/consent/give", method = RequestMethod.POST)
    @ResponseBody
    public ConsentResponse giveConsent() {
        Citizen citizen = citizenService.getCitizen();
        LOG.debug("Requesting 'giveConsent' for citizen {0}", citizen.getUsername());
        citizen.setConsent(consentService.setConsent(citizen.getUsername(), true));
        return new ConsentResponse(true);
    }

    @RequestMapping(value = "/consent/revoke", method = RequestMethod.POST)
    @ResponseBody
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
