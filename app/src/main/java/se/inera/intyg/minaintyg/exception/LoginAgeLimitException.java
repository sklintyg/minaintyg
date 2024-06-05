package se.inera.intyg.minaintyg.exception;

import org.springframework.security.core.AuthenticationException;

public class LoginAgeLimitException extends AuthenticationException {

  public LoginAgeLimitException(String message) {
    super(message);
  }
}
