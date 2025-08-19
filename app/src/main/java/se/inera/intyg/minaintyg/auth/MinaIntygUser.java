package se.inera.intyg.minaintyg.auth;

import static se.inera.intyg.minaintyg.auth.AuthenticationConstants.ELEG_PARTY_REGISTRATION_ID;
import static se.inera.intyg.minaintyg.auth.AuthenticationConstants.UNKNOWN_PARTY_REGISTRATION_ID;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinaIntygUser implements Saml2AuthenticatedPrincipal, Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private String userId;
  private String userName;
  private LoginMethod loginMethod;

  @Override
  public String getName() {
    return getUserName();
  }

  @Override
  public String getRelyingPartyRegistrationId() {
    return LoginMethod.ELVA77.equals(loginMethod)
        ? ELEG_PARTY_REGISTRATION_ID
        : UNKNOWN_PARTY_REGISTRATION_ID;
  }
}
