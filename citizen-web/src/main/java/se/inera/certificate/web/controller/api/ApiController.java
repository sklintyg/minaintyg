package se.inera.certificate.web.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "")
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        log.debug("api.test");
        return "test";
    }

}
