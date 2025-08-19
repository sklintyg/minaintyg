package se.inera.intyg.minaintyg.exception;

import se.inera.intyg.minaintyg.auth.LoginMethod;

public class UserInactiveException extends RuntimeException {

  private LoginMethod loginMethod;

  public UserInactiveException(String message, LoginMethod loginMethod) {
    super(message);
    this.loginMethod = loginMethod;
  }

  public LoginMethod loginMethod() {
    return loginMethod;
  }
}
