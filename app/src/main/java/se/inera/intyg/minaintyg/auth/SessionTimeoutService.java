/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SessionTimeoutService {

  @Value("${spring.session.timeout.limit.minutes}")
  private Integer timeoutLimitInMinutes;

  @Value("#{'${spring.session.timeout.reset.exluded.urls}'.split(',')}")
  private List<String> excludedUrls;

  public void checkSessionValidity(HttpServletRequest request) {
    final var session = request.getSession(false);
    if (session == null) {
      return;
    }

    if (isSessionExpired(session)) {
      log.info("Invalidating session due to inactivity.");
      session.invalidate();
      return;
    }

    updateSession(session, request, excludedUrls);
  }

  private void updateSession(
      HttpSession session, HttpServletRequest request, List<String> excludedUrls) {
    if (isExcludedURL(request, excludedUrls)) {
      session.setAttribute(SessionConstants.SECONDS_UNTIL_EXPIRE, getExpirationTime(session));
      return;
    }

    session.setAttribute(
        SessionConstants.SECONDS_UNTIL_EXPIRE, getSeconds(sessionTimeoutLimitInMillis()));
    session.setAttribute(SessionConstants.LAST_ACCESS_ATTRIBUTE, System.currentTimeMillis());
  }

  private static Long getSeconds(Long ms) {
    return TimeUnit.MILLISECONDS.toSeconds(ms);
  }

  private static boolean isExcludedURL(HttpServletRequest request, List<String> excludedUrls) {
    return excludedUrls.stream().anyMatch(url -> request.getRequestURI().contains(url));
  }

  private static Long getLastAccessedTime(HttpSession session) {
    return session.getAttribute(SessionConstants.LAST_ACCESS_ATTRIBUTE) != null
        ? (Long) session.getAttribute(SessionConstants.LAST_ACCESS_ATTRIBUTE)
        : System.currentTimeMillis();
  }

  private Long getExpirationTime(HttpSession session) {
    final var inactiveTime = System.currentTimeMillis() - getLastAccessedTime(session);
    return getSeconds(sessionTimeoutLimitInMillis() - inactiveTime);
  }

  private boolean isSessionExpired(HttpSession session) {
    final var inactiveTime = System.currentTimeMillis() - getLastAccessedTime(session);
    return inactiveTime > sessionTimeoutLimitInMillis();
  }

  private Long sessionTimeoutLimitInMillis() {
    return TimeUnit.MINUTES.toMillis(timeoutLimitInMinutes);
  }
}
