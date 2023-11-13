package se.inera.intyg.minaintyg.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;

@Slf4j
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private final String errorLoginUrl;
  private final MonitoringLogService monitoringLogService;

  public CustomAuthenticationFailureHandler(@Value("${error.login.url}") String errorLoginUrl,
      MonitoringLogService monitoringLogService) {
    this.errorLoginUrl = errorLoginUrl;
    this.monitoringLogService = monitoringLogService;
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    final var errorId = String.valueOf(UUID.randomUUID());
    log.error(
        String.format("Failed login attempt with errorId '%s' - exception %s", errorId, exception)
    );
    monitoringLogService.logUserLoginFailed(exception.getMessage());
    //TODO: Add client log
    request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
    request.getRequestDispatcher(getUrl(errorId)).forward(request, response);
  }

  private String getUrl(String errorId) {
    return errorLoginUrl.replace("{errorId}", errorId);
  }
}
