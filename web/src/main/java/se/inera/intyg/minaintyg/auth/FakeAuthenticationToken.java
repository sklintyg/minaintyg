package se.inera.intyg.minaintyg.auth;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class FakeAuthenticationToken extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = -2796850504529240890L;

    private FakeCredentials fakeCredentials;
    private final Set<SimpleGrantedAuthority> roles = Collections.singleton(new SimpleGrantedAuthority("ROLE_ORGANIZATION_DELEGATE"));

    private final Object principal;

    public FakeAuthenticationToken(FakeCredentials fakeCredentials, Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.fakeCredentials = fakeCredentials;
        this.principal = principal;
        setAuthenticated(true);
    }

    public Set<SimpleGrantedAuthority> getRoles() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return fakeCredentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.fakeCredentials = null;
    }

    @Override
    public String toString() {
        return "FakeAuthenticationToken{" +
            "fakeCredentials=" + fakeCredentials +
            '}';
    }
}
