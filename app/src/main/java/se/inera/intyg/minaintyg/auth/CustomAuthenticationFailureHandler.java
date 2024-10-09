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
import se.inera.intyg.minaintyg.exception.LoginAgeLimitException;
import se.inera.intyg.minaintyg.logging.service.MonitoringLogService;

@Slf4j
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private final String errorLoginUrl;
  private final String errorLoginUnderageUrl;
  private final MonitoringLogService monitoringLogService;

  public CustomAuthenticationFailureHandler(@Value("${error.login.url}") String errorLoginUrl,
      @Value("${error.login.underage.url}") String errorLoginUnderageUrl,
      MonitoringLogService monitoringLogService) {
    this.errorLoginUrl = errorLoginUrl;
    this.errorLoginUnderageUrl = errorLoginUnderageUrl;
    this.monitoringLogService = monitoringLogService;
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException {

    if (exception.getCause() instanceof LoginAgeLimitException loginAgeLimitException) {
      monitoringLogService.logUserLoginFailed(
          loginAgeLimitException.getMessage(),
          loginAgeLimitException.loginMethod().value()
      );
      response.sendRedirect(request.getContextPath() + errorLoginUnderageUrl);
      return;
    }

    final var errorId = String.valueOf(UUID.randomUUID());
    handleLogEvents(response, exception, errorId);
    response.sendRedirect(request.getContextPath() + addErrorIdToUrl(errorId));
  }

  private void handleLogEvents(HttpServletResponse response, AuthenticationException exception,
      String errorId) {
    log.error(
        String.format("Failed login attempt with errorId '%s' - exception %s", errorId, exception)
    );
    monitoringLogService.logUserLoginFailed(exception.getMessage(), "-");
    monitoringLogService.logClientError(errorId, String.valueOf(response.getStatus()),
        exception.getMessage(), null);
  }

  private String addErrorIdToUrl(String errorId) {
    return errorLoginUrl.replace("{errorId}", errorId);
  }
}
