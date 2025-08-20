package se.inera.intyg.minaintyg.exception;

import se.inera.intyg.minaintyg.auth.LoginMethod;

public class CitizenInactiveException extends RuntimeException {

  private LoginMethod loginMethod;

  public CitizenInactiveException(String message, LoginMethod loginMethod) {
    super(message);
    this.loginMethod = loginMethod;
  }

  public LoginMethod loginMethod() {
    return loginMethod;
  }
}
