package se.inera.intyg.minaintyg.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private static final String ERROR_LOGIN_URL = "/error/login/";
  private final MonitoringLogService monitoringLogService;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    final var errorId = String.valueOf(UUID.randomUUID());
    monitoringLogService.logUserLoginFailed(errorId, exception.getMessage());
    request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
    request.getRequestDispatcher(ERROR_LOGIN_URL + errorId).forward(request, response);
  }
}
