/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.web.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.minaintyg.web.web.service.CitizenService;
import se.inera.intyg.minaintyg.web.web.service.ConsentService;

public class VerifyConsentInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(VerifyConsentInterceptor.class);

    @Autowired
    private ConsentService consentService;

    @Autowired
    private CitizenService citizenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        LOG.debug("Verifying citizen consent...");

        // Get Citizen instance from context
        Citizen citizen = citizenService.getCitizen();
        if (citizen == null) {
            LOG.error("State of consent not known -citizen was null");
            return false;
        }

        LOG.debug("Login from " + citizen.getLoginMethod().toString());
        if (!citizen.consentIsKnown()) {
            LOG.debug("State of consent not known - fetching consent status...");
            boolean consentResult = consentService.fetchConsent(new Personnummer(citizen.getUsername()));
            LOG.debug("Consent result is {}", consentResult);
            // set the consent result so that we don't have to fetch it next time around
            citizen.setConsent(consentResult);
        }

        // Check consent state of citizen
        if (!citizen.hasConsent()) {
            LOG.warn("User: {} does not have consent", new Personnummer(citizen.getUsername()).getPnrHash());
            response.sendRedirect("/web/visa-ge-samtycke#/consent");
            // return false to indicate that the request/filter chain should stop here.
            // We have already taken care of a respone to the client.
            return false;
        }
        // We have a consent, let the request continue processing
        return true;
    }

}
