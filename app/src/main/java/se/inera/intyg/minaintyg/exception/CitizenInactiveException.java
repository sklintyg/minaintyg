package se.inera.intyg.minaintyg.exception;

import org.springframework.security.core.AuthenticationException;
import se.inera.intyg.minaintyg.auth.LoginMethod;

public class CitizenInactiveException extends AuthenticationException {

  private LoginMethod loginMethod;

  public CitizenInactiveException(String message, LoginMethod loginMethod) {
    super(message);
    this.loginMethod = loginMethod;
  }

  public LoginMethod loginMethod() {
    return loginMethod;
  }
}
