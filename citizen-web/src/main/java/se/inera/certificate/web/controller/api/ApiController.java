package se.inera.certificate.web.controller.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import se.inera.certificate.api.CertificateMeta;
import se.inera.certificate.web.security.Citizen;
import se.inera.certificate.web.service.CertificateService;
import se.inera.certificate.web.service.CitizenService;

@Controller
@RequestMapping(value = "/certificates", produces = "application/json")
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private CertificateService certificateService;
    
    @Autowired
    private CitizenService citizenService;
    
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        log.debug("api.test");
        return "test";
    }
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<CertificateMeta> listCertificates() {
        Citizen citizen = citizenService.getCitizen();
        return certificateService.getCertificates(citizen.getUsername());
    }

}
