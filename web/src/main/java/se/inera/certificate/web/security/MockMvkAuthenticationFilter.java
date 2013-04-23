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
package se.inera.certificate.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.callistasoftware.netcare.mvk.authentication.service.api.AuthenticationResult;
import org.callistasoftware.netcare.mvk.authentication.service.api.impl.AuthenticationResultImpl;
import org.callistasoftware.netcare.mvk.authentication.web.filter.AuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

public class MockMvkAuthenticationFilter extends
        AbstractPreAuthenticatedProcessingFilter {

    private static final Logger log = LoggerFactory
            .getLogger(MockMvkAuthenticationFilter.class);

    private static final String guidParameterName = "guid";

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "n/a";
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        log.info("Getting preauthenticated principal");
        final Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (auth == null) {
            log.debug("Authentication was null, check for guid parameter");
            final String guid = request.getParameter(MockMvkAuthenticationFilter.guidParameterName);
            if (guid != null) {
                log.debug(
                        "Guid parameter found. Mocking validation against MVK as {}...",
                        guid);
                AuthenticationResult mockedResult = AuthenticationResultImpl
                        .newPatient(guid);
                return mockedResult;

            }
        } else {
            log.debug("Authentication found. Proceed...");
            return auth.getPrincipal();
        }

        log.warn("Reached end of processing and still no authentication. Return null...");
        return null;
    }

}