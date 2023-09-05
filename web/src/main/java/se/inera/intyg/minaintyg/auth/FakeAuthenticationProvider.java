package se.inera.intyg.minaintyg.auth;

import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
@RequiredArgsConstructor
public class FakeAuthenticationProvider implements AuthenticationProvider {

    private final MinaIntygUserDetailService minaIntygUserDetailService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof FakeAuthenticationToken)) {
            return null;
        }
        final var credentials = (FakeCredentials) authentication.getCredentials();
        final var principal = minaIntygUserDetailService.getPrincipal(credentials.getPersonId());
        return new FakeAuthenticationToken(credentials, principal, buildGrantedAuthorities(principal));
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
