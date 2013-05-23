package se.inera.certificate.web.controller.moduleapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import se.inera.certificate.api.CertificateMeta;
import se.inera.certificate.web.service.CertificateService;
import se.inera.certificate.web.service.CitizenService;

@Controller
@RequestMapping(value = "/certificate", produces = "application/json")
public class ModuleApiController {

    private static final Logger LOG = LoggerFactory.getLogger(ModuleApiController.class);

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private CitizenService citizenService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CertificateMeta getCertificate(@PathVariable(value = "id") String id) {
        LOG.debug("getCertificate: {}",id);
        CertificateMeta mock = new CertificateMeta();
        mock.setId(id);
        mock.setType("RLI");
        mock.setCareunitName("En vårdenhet");
        
        return mock;
       
    }

   

}
