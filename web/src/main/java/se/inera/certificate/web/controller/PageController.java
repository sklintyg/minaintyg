package se.inera.certificate.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "")
public class PageController {
	 @RequestMapping(value = "/start", method = RequestMethod.GET)
	    public ModelAndView displayLoginInfo() {
		  return new ModelAndView("start"); 
	    } 

}
