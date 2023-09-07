package se.inera.intyg.minaintyg.auth;

import org.springframework.security.core.AuthenticationException;

public class PUServiceException extends AuthenticationException {

  public PUServiceException(String message) {
    super(message);
  }
}