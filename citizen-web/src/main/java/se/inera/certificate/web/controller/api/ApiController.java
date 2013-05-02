package se.inera.certificate.web.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/certificates", produces = "application/json")
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        log.debug("api.test");
        return "test";
    }
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public String listCertificates() {
       
        return "{ \"resultCode\":\"OK\"," +
        		"\"certificateList\": [" +
                "{\"date\":\"2012-01-01\", \"period\":\"2015-2015\"}," +
                "{\"date\":\"2013-02-11\", \"period\":\"2015-2015\"},"+
                "{\"date\":\"2014-03-21\", \"period\":\"2016-2016\"},"+
                "{\"date\":\"2013-02-11\", \"period\":\"2015-2015\"},"+
                "{\"date\":\"2014-03-21\", \"period\":\"2016-2016\"},"+
                "{\"date\":\"2013-02-11\", \"period\":\"2015-2015\"},"+
                "{\"date\":\"2014-03-21\", \"period\":\"2016-2016\"},"+
                "{\"date\":\"2013-02-11\", \"period\":\"2015-2015\"},"+
                "{\"date\":\"2014-03-21\", \"period\":\"5516-1016\"},"+
                "{\"date\":\"2013-02-11\", \"period\":\"2015-2015\"},"+
                "{\"date\":\"2014-03-21\", \"period\":\"5516-1016\"},"+
                "{\"date\":\"2014-03-21\", \"period\":\"5516-1016\"},"+
                "{\"date\":\"2014-03-21\", \"period\":\"2016-2016\"},"+
                "{\"date\":\"2013-02-11\", \"period\":\"2015-2015\"},"+
                "{\"date\":\"2014-03-21\", \"period\":\"2016-2016\"},"+
                "{\"date\":\"2015-04-31\", \"period\":\"2017-2016\"}"+
        		"]}";
    }

}
