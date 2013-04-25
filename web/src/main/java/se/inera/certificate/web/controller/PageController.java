/**
 * Copyright (C) 2012 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Web (http://code.google.com/p/inera-certificate-web).
 *
 * Inera Certificate Web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import se.inera.certificate.web.security.Citizen;
import se.inera.certificate.web.service.ConsentService;

@Controller
@RequestMapping(value = "")
public class PageController {

    private static final Logger log = LoggerFactory.getLogger(PageController.class);

    @Autowired
    private ConsentService consentService;

    @RequestMapping(value = "/sso", method = RequestMethod.GET)
    public String sso() {
        log.debug("sso");
        Citizen citizen = (Citizen) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        citizen.setConsent(consentService.fetchConsent(citizen.getUsername()));
        return "redirect:/web/start";
    }

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public ModelAndView displayStart() {
        log.debug("displayStart");
        return new ModelAndView("start");
    }

    @RequestMapping(value = "/visa-ge-samtycke", method = RequestMethod.GET)
    public ModelAndView displayConsentForm() {
        log.debug("displayConsentForm");
        return new ModelAndView("consent-form");
    }

}
