package se.inera.intyg.minaintyg.auth;

import org.springframework.security.core.AuthenticationException;

public class PersonNotFoundException extends AuthenticationException {

    public PersonNotFoundException(String message) {
        super(message);
    }
}