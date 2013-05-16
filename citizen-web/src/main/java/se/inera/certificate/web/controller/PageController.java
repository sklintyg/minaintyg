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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import se.inera.certificate.web.security.Citizen;
import se.inera.certificate.web.service.CitizenService;
import se.inera.certificate.web.service.ConsentService;

@Controller
@RequestMapping(value = "")
public class PageController {

    private static final Logger LOG = LoggerFactory.getLogger(PageController.class);

    @Autowired
    private ConsentService consentService;

    @Autowired
    private CitizenService citizenService;

    @RequestMapping(value = { "/sso", "/fakesso" }, method = RequestMethod.GET)
    public String sso() {
        LOG.debug("sso");
        Citizen citizen = citizenService.getCitizen();
        // fetch and set consent status
        citizen.setConsent(consentService.fetchConsent(citizen.getUsername()));
        return "redirect:/web/start";
    }

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public ModelAndView displayStart() {
        LOG.debug("displayStart");
        return new ModelAndView("start");
    }

    @RequestMapping(value = "/visa-ge-samtycke", method = RequestMethod.GET)
    public ModelAndView displayConsentForm() {
        LOG.debug("displayConsentForm");
        return new ModelAndView("consent-form");
    }

    @RequestMapping(value = "/ge-samtycke", method = RequestMethod.POST)
    public ModelAndView setConsent() {
        LOG.debug("setConsent");
        // update consent in security consent
        Citizen citizen = citizenService.getCitizen();
        // Set and set consent status
        citizen.setConsent(consentService.setConsent(citizen.getUsername(), true));
        LOG.debug("consent after setConsent {}", citizen.hasConsent());
        return new ModelAndView("start");
    }

}
