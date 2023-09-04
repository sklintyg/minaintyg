package se.inera.intyg.minaintyg.auth;

import java.io.Serial;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class FakeAuthenticationToken extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = -2796850504529240890L;

    private final FakeCredentials fakeCredentials;

    public FakeAuthenticationToken(FakeCredentials fakeCredentials) {
        super(null);
        this.fakeCredentials = fakeCredentials;
    }

    @Override
    public Object getCredentials() {
        return fakeCredentials;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public String toString() {
        return "FakeAuthenticationToken{" +
            "fakeCredentials=" + fakeCredentials +
            '}';
    }
}
