/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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

package se.inera.intyg.minaintyg.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SessionTimeoutService {

    public static final String LAST_ACCESS_ATTRIBUTE = "LastAccess";
    public static final String SECONDS_UNTIL_EXPIRE = "SecondsUntilExpire";
    public static final Long SESSION_EXPIRATION_LIMIT = TimeUnit.MINUTES.toMillis(25);


    public void checkSessionValidity(HttpServletRequest request, List<String> excludedUrls) {
        final var session = request.getSession(false);

        if (isSessionExpired(session)) {
            log.info("Invalidating session.");
            session.invalidate();
            return;
        }

        updateSession(session, request, excludedUrls);
    }

    private static void updateSession(
        HttpSession session,
        HttpServletRequest request,
        List<String> excludedUrls) {
        if (isExcludedURL(request, excludedUrls)) {
            session.setAttribute(SECONDS_UNTIL_EXPIRE, getExpirationTime(session));
            return;
        }

        session.setAttribute(SECONDS_UNTIL_EXPIRE, SESSION_EXPIRATION_LIMIT);
        session.setAttribute(LAST_ACCESS_ATTRIBUTE, System.currentTimeMillis());
    }

    private static boolean isExcludedURL(HttpServletRequest request, List<String> excludedUrls) {
        return excludedUrls.stream().anyMatch(url -> url.equals(request.getRequestURI()));
    }

    private static Long getLastAccessedTime(HttpSession session) {
        return session.getAttribute(LAST_ACCESS_ATTRIBUTE) != null
            ? (Long) session.getAttribute(LAST_ACCESS_ATTRIBUTE) : 0;
    }

    private static Long getExpirationTime(HttpSession session) {
        final var inactiveTime =
            System.currentTimeMillis() - getLastAccessedTime(session);
        return SESSION_EXPIRATION_LIMIT - inactiveTime;
    }

    private static boolean isSessionExpired(HttpSession session) {
        final var inactiveTime = System.currentTimeMillis() - getLastAccessedTime(session);
        return inactiveTime > SESSION_EXPIRATION_LIMIT;
    }

}
