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
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.exception.CitizenInactiveException;
import se.inera.intyg.minaintyg.exception.LoginAgeLimitException;
import se.inera.intyg.minaintyg.logging.service.MonitoringLogService;

@Slf4j
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private final String errorLoginUrl;
  private final String errorLoginUnderageUrl;
  private final String errorLoginInactiveUrl;
  private final MonitoringLogService monitoringLogService;

  public CustomAuthenticationFailureHandler(
      @Value("${error.login.url}") String errorLoginUrl,
      @Value("${error.login.underage.url}") String errorLoginUnderageUrl,
      MonitoringLogService monitoringLogService,
      @Value("${error.login.inactive.url}") String errorLoginInactiveUrl) {
    this.errorLoginUrl = errorLoginUrl;
    this.errorLoginUnderageUrl = errorLoginUnderageUrl;
    this.monitoringLogService = monitoringLogService;
    this.errorLoginInactiveUrl = errorLoginInactiveUrl;
  }

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {

    if (exception.getCause() instanceof LoginAgeLimitException loginAgeLimitException) {
      monitoringLogService.logUserLoginFailed(
          loginAgeLimitException.getMessage(), loginAgeLimitException.loginMethod().value());
      response.sendRedirect(request.getContextPath() + errorLoginUnderageUrl);
      return;
    }

    if (exception.getCause() instanceof CitizenInactiveException citizenInactiveException) {
      monitoringLogService.logUserLoginFailed(
          citizenInactiveException.getMessage(), citizenInactiveException.loginMethod().value());
      response.sendRedirect(request.getContextPath() + errorLoginInactiveUrl);
      return;
    }

    final var errorId = String.valueOf(UUID.randomUUID());
    handleLogEvents(response, exception, errorId);
    response.sendRedirect(request.getContextPath() + addErrorIdToUrl(errorId));
  }

  private void handleLogEvents(
      HttpServletResponse response, AuthenticationException exception, String errorId) {
    log.error(
        String.format("Failed login attempt with errorId '%s' - exception %s", errorId, exception));
    monitoringLogService.logUserLoginFailed(exception.getMessage(), "-");
    monitoringLogService.logClientError(
        errorId, String.valueOf(response.getStatus()), exception.getMessage(), null);
  }

  private String addErrorIdToUrl(String errorId) {
    return errorLoginUrl.replace("{errorId}", errorId);
  }
}
