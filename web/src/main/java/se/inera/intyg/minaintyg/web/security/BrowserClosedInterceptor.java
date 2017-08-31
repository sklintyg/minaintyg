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
package se.inera.intyg.minaintyg.web.security;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;

import javax.servlet.http.*;

import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Filter som kontrollerar om användaren har stängt webbläsaren - finns ingen
 * timestamp i HttpSession ignoreras anropet - om timestamp finns och är äldre
 * än timeoutSeconds loggas användaren ut och redirectas till redirectLocation -
 * om timestamp finns och är yngre än timeoutSeconds plockas timestampen bort
 * från session och requesten går igenom.
 */
public class BrowserClosedInterceptor extends HandlerInterceptorAdapter {
    public static final String BROWSER_CLOSED_TIMESTAMP = "BROWSER_CLOSED_TIMESTAMP";
    private static final Logger LOG = LoggerFactory.getLogger(BrowserClosedInterceptor.class);

    private Clock systemClock = Clock.systemDefaultZone();

    private LogoutHandler logoutHandler;
    private String redirectLocation;
    private Integer timeoutSeconds;

    @VisibleForTesting
    void setMockSystemClock(Clock systemClock) {
        this.systemClock = systemClock;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws IOException {

        HttpSession session = request.getSession();
        LocalDateTime then = (LocalDateTime) session.getAttribute(BROWSER_CLOSED_TIMESTAMP);

        if (then != null) {
            if (then.plusSeconds(timeoutSeconds).isBefore(LocalDateTime.now(systemClock))) {
                LOG.warn("Browser closed and protected page revisited, user logged out");
                // log out user
                logoutHandler.logout(request, response, null);
                response.sendRedirect(redirectLocation);
                return false;
            } else {
                // valid reqest remove timestamp
                session.removeAttribute(BROWSER_CLOSED_TIMESTAMP);
                LOG.debug("Valid refresh of browser");
                return true;
            }
        }
        // normal request
        return true;
    }

    public void setTimeoutSeconds(Integer timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public void setLogoutHandler(LogoutHandler logoutHandler) {
        this.logoutHandler = logoutHandler;
    }

    public void setRedirectLocation(String redirectLocation) {
        this.redirectLocation = redirectLocation;
    }
}
