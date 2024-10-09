package se.inera.intyg.minaintyg.exception;

import org.springframework.security.core.AuthenticationException;
import se.inera.intyg.minaintyg.auth.LoginMethod;

public class LoginAgeLimitException extends AuthenticationException {

  private LoginMethod loginMethod;

  public LoginAgeLimitException(String message, LoginMethod loginMethod) {
    super(message);
    this.loginMethod = loginMethod;
  }

  public LoginMethod loginMethod() {
    return loginMethod;
  }
}
