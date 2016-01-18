/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.minaintyg.web.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.minaintyg.web.web.security.Citizen;
import se.inera.intyg.minaintyg.web.web.service.CitizenService;
import se.inera.intyg.minaintyg.web.web.service.ConsentService;

@Controller
@RequestMapping(value = "")
public class PageController {

    private static final Logger LOG = LoggerFactory.getLogger(PageController.class);

    @Autowired
    private Environment environment;

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

    @RequestMapping(value = { "/sso" }, method = RequestMethod.GET)
    public String sso() {
        LOG.debug("sso");
        Citizen citizen = citizenService.getCitizen();
        // fetch and set consent status
        citizen.setConsent(consentService.fetchConsent(new Personnummer(citizen.getUsername())));
        return "redirect:/web/start";
    }

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public ModelAndView displayStart() {
        ModelAndView modelAndView = new ModelAndView("start");
        populateUseMinifiedJavaScript(modelAndView);
        LOG.debug("displayStart");
        return modelAndView;
    }

    @RequestMapping(value = "/visa-ge-samtycke", method = RequestMethod.GET)
    public ModelAndView displayConsentForm() {
        ModelAndView modelAndView = new ModelAndView("consent-form");
        populateUseMinifiedJavaScript(modelAndView);
        LOG.debug("displayConsentForm");
        return modelAndView;
    }

    @RequestMapping(value = { "/tillbaka-till-mvk" }, method = RequestMethod.GET)
    public String tillbakaTillMvk(HttpServletRequest request) {
        LOG.debug("tillbakaTillMvk");
        invalidateSessionAndClearContext(request);
        return "redirect:" + mvkStartUrl;
    }

    @RequestMapping(value = { "/logga-ut" }, method = RequestMethod.GET)
    public String loggaUt(HttpServletRequest request) {
        LOG.debug("loggaUt");
        invalidateSessionAndClearContext(request);
        return "redirect:" + mvkLogoutUrl;
    }

    @RequestMapping(value = { "/logga-ut-fk" }, method = RequestMethod.GET)
    public ModelAndView loggaUtFk(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("logout-fk");
        populateUseMinifiedJavaScript(modelAndView);
        LOG.debug("loggaUtFk");
        invalidateSessionAndClearContext(request);
        return modelAndView;
    }

    protected void invalidateSessionAndClearContext(HttpServletRequest request) {
        request.getSession().invalidate();
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
    }

    public void populateUseMinifiedJavaScript(ModelAndView model) {
        model.addObject("useMinifiedJavaScript", environment.getProperty("minaintyg.useMinifiedJavaScript", "true"));
    }
}
