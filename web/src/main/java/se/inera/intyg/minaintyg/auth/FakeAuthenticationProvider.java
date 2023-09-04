package se.inera.intyg.minaintyg.auth;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

public class FakeAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof FakeAuthenticationToken)) {
            return null;
        }
        final var credentials = (FakeCredentials) authentication.getCredentials();
        final var principal = new MinaIntygUser(credentials.getPersonId(), credentials.getPersonName());
        //TODO: Might need to fix so that credentials are updated on the result from UsernamePasswordAuth...
        return new UsernamePasswordAuthenticationToken(principal, credentials, buildGrantedAuthorities(principal));
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return FakeAuthenticationToken.class.isAssignableFrom(authentication);
    }
    private Collection<? extends GrantedAuthority> buildGrantedAuthorities(Object details) {
        if (details instanceof MinaIntygUser) {
            return ((MinaIntygUser) details).getAuthorities();
        } else {
            return new ArrayList<>();
        }
    }
}
