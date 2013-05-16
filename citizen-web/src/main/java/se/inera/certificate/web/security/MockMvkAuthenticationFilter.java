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

import javax.servlet.http.HttpServletRequest;

import org.callistasoftware.netcare.mvk.authentication.service.api.impl.AuthenticationResultImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

public class MockMvkAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    private static final Logger LOG = LoggerFactory.getLogger(MockMvkAuthenticationFilter.class);

    private static final String MVK_TOKEN_PARAMETER_NAME = "guid";

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "n/a";
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        LOG.info("Getting preauthenticated principal");
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            LOG.debug("Authentication was null, check for guid parameter");
            final String guid = request.getParameter(MockMvkAuthenticationFilter.MVK_TOKEN_PARAMETER_NAME);
            if (guid != null) {
                LOG.debug("Guid parameter found. Mocking validation against MVK as {}...", guid);
                return AuthenticationResultImpl.newPatient(guid);
            }
        } else {
            LOG.debug("Authentication found. Proceed...");
            return auth.getPrincipal();
        }

        LOG.warn("Reached end of processing and still no authentication. Return null...");
        return null;
    }

}
