/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.controller.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import se.inera.intyg.minaintyg.web.filters.SessionTimeoutFilter;

/**
 * Reports basic information about the current session status.
 * This controller works in cooperation with SessionTimeoutFilter that makes sure that requests to:
 * <ul>
 * <li>getSessionStatus does NOT extend the session</li>
 * <li>getExtendSession does extend the session.</li>
 * </ul>
 *
 * @see SessionTimeoutFilter
 * @see org.springframework.security.web.context.SecurityContextRepository SecurityContextRepository
 * @see org.springframework.security.web.context.HttpSessionSecurityContextRepository
 * HttpSessionSecurityContextRepository
 */

public class SessionStatusController {

    public static final String SESSION_STATUS_REQUEST_MAPPING = "/session/session-auth-check";
    public static final String SESSION_STATUS_PING = "/ping";

    public static final String SESSION_STATUS_CHECK_URI = SESSION_STATUS_REQUEST_MAPPING + SESSION_STATUS_PING;

    private static final String CHARSET_UTF_8 = ";charset=utf-8";
    private static final String JSON_UTF8 = MediaType.APPLICATION_JSON + CHARSET_UTF_8;

    @GET
    @Path(SessionStatusController.SESSION_STATUS_PING)
    @Produces(JSON_UTF8)
    public GetSessionStatusResponse getSessionStatus(@Context HttpServletRequest request) {
        return createStatusResponse(request);
    }

    @GET
    @Path("/extend")
    @Produces(JSON_UTF8)
    public GetSessionStatusResponse getExtendSession(@Context HttpServletRequest request) {

        return createStatusResponse(request);
    }

    private GetSessionStatusResponse createStatusResponse(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // The sessionTimeoutFilter should have put a secondsLeft attribute in the request for us to use.
        Long secondsLeft = (Long) request.getAttribute(SessionTimeoutFilter.SECONDS_UNTIL_SESSIONEXPIRE_ATTRIBUTE_KEY);

        return new GetSessionStatusResponse(session != null, hasAuthenticatedPrincipalSession(session),
            secondsLeft == null ? 0 : secondsLeft);
    }

    private boolean hasAuthenticatedPrincipalSession(HttpSession session) {
        if (session != null) {
            final Object context = session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            if (context instanceof SecurityContext) {
                SecurityContext securityContext = (SecurityContext) context;
                return securityContext.getAuthentication() != null && securityContext.getAuthentication().getPrincipal() != null;
            }

        }
        return false;
    }

}
