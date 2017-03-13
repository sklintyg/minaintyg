package se.inera.intyg.minaintyg.web.web.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLAuthenticationToken;

/**
 * Basic implementation of {@link SAMLAuthenticationProvider} for Mina Intyg.
 *
 * Will only accept {@link SAMLAuthenticationToken}, e.g. not usable for Fake logins!
 *
 * Created by eriklupander on 2017-03-09.
 */
public class MinaIntygAuthenticationProvider extends SAMLAuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return super.authenticate(authentication);
    }

    @Override
    public boolean supports(Class aClass) {
        return SAMLAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
