package se.inera.intyg.minaintyg.web.web.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLAuthenticationToken;

/**
 * Created by eriklupander on 2017-03-09.
 */
public class MinaIntygAuthenticationProvider extends SAMLAuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(MinaIntygAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LOG.info("ENTER - MinaIntygAuthenticationProvider#authenticate");
        return super.authenticate(authentication);
    }

    @Override
    public boolean supports(Class aClass) {
        LOG.info("ENTER - MinaIntygAuthenticationProvider#supports");
        return SAMLAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
