/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
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
package se.inera.certificate.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import se.inera.certificate.web.service.CitizenService;
import se.inera.certificate.web.service.ConsentService;

/**
 */
public class VerifyConsentInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(VerifyConsentInterceptor.class);

    private boolean jsonResponse;

    @Autowired
    private ConsentService consentService;

    @Autowired
    private CitizenService citizenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.debug("Verifying citizen consent...");

        // Get Citizen instance from context
        Citizen citizen = citizenService.getCitizen();
        if (!citizen.consentIsKnown()) {
            log.debug("State of consent not known - fetching consent status...");
            boolean consentResult = consentService.fetchConsent(citizen.getUsername());
            log.debug("Consent result is {}", consentResult);
            //set the consent result so that we don't have to fetch it next time around
            citizen.setConsent(consentResult);
        }

        // Check consent state of citizen
        if (!citizen.hasConsent()) {
            // depending on how this interceptor is configured, we should reply as json or browser-page redirect
            if (jsonResponse) {
                response.getOutputStream().print("Inget samtycke.json");
            } else {
                response.sendRedirect("/web/visa-ge-samtycke");
            }
            // return false to indicate that the request/filter chain should stop here.
            // We have already taken care of a respone to the client.
            return false;
        }
        // We have a consent, let the request continue processing
        return true;
    }

    public void setJsonResponse(boolean jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    public boolean getJsonResponse() {
        return jsonResponse;
    }
}
