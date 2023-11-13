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

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private static final String ERROR_LOGIN_URL = "/error/login/";

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    final var id = String.valueOf(UUID.randomUUID());
    request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
    request.getRequestDispatcher(ERROR_LOGIN_URL + id).forward(request, response);
  }
}
