package se.inera.intyg.minaintyg.auth;

import static se.inera.intyg.minaintyg.auth.AuthenticationConstants.PERSON_ID_ATTRIBUTE;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.saml2.provider.service.authentication.DefaultSaml2AuthenticatedPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;

public class Saml2AuthenticationToken extends AbstractAuthenticationToken {

    private final Saml2Authentication saml2Authentication;
    public Saml2AuthenticationToken(Saml2Authentication authentication) {
        super(authentication.getAuthorities());
        this.saml2Authentication = authentication;
    }

    @Override
    public Object getCredentials() {
        return saml2Authentication.getCredentials();
    }

    @Override
    public Object getPrincipal() {
        if (saml2Authentication.isAuthenticated()) {
            final var personId = getAttribute(saml2Authentication);
            // TODO: verify this info from PU
            return new MinaIntygUser(personId, "nameFromPU");
        }
        return null;
    }

    private String getAttribute(Saml2Authentication samlCredential) {
        final var principal = (DefaultSaml2AuthenticatedPrincipal) samlCredential.getPrincipal();
        final var attributes = principal.getAttributes();
        if (attributes.containsKey(PERSON_ID_ATTRIBUTE)) {
            return (String) attributes.get(PERSON_ID_ATTRIBUTE).get(0);
        }
        throw new IllegalArgumentException("Could not extract attribute '" + PERSON_ID_ATTRIBUTE + "' from Saml2Authentication.");
    }
}
