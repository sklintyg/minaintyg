package se.inera.intyg.minaintyg.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;

public class Saml2AuthenticationToken extends AbstractAuthenticationToken {

  private final Object principal;
  private final Saml2Authentication saml2Authentication;

  public Saml2AuthenticationToken(Object principal, Saml2Authentication authentication) {
    super(authentication.getAuthorities());
    this.principal = principal;
    this.saml2Authentication = authentication;
  }

  @Override
  public Object getCredentials() {
    return saml2Authentication.getCredentials();
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }
}
