package se.inera.intyg.minaintyg.auth;

import static se.inera.intyg.minaintyg.config.WebSecurityConfig.ELEG;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinaIntygUser implements Saml2AuthenticatedPrincipal, Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private final Set<SimpleGrantedAuthority> roles = Collections.singleton(
      new SimpleGrantedAuthority("ROLE_ORGANIZATION_DELEGATE"));
  private String personId;
  private String personName;
  private LoginMethod loginMethod;

  @Override
  public String getName() {
    return null;
  }

  @Override
  public String getRelyingPartyRegistrationId() {
    return ELEG;
  }
}
