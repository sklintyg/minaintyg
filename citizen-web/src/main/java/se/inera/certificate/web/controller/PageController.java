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

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    @Value("${mvk.url.start}")
    private String mvkStartUrl;

    @Autowired
    @Value("${mvk.url.logout}")
    private String mvkLogoutUrl;

    @RequestMapping(value = {"/sso" }, method = RequestMethod.GET)
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

    @RequestMapping(value = {"/tillbaka-till-mvk" }, method = RequestMethod.GET)
    public String tillbakaTillMvk(HttpServletRequest request) {
        LOG.debug("tillbakaTillMvk");
        invalidateSessionAndClearContext(request);
        return "redirect:" + mvkStartUrl;
    }

    @RequestMapping(value = {"/logga-ut" }, method = RequestMethod.GET)
    public String loggaUt(HttpServletRequest request) {
        LOG.debug("loggaUt");
        invalidateSessionAndClearContext(request);
        return "redirect:" + mvkLogoutUrl;
    }

    @RequestMapping(value = {"/logga-ut-fk" }, method = RequestMethod.GET)
    public ModelAndView loggaUtFk(HttpServletRequest request) {
        LOG.debug("loggaUtFk");
        invalidateSessionAndClearContext(request);
        return new ModelAndView("logout-fk");
    }

    protected void invalidateSessionAndClearContext(HttpServletRequest request) {
        request.getSession().invalidate();
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
    }

}
