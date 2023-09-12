package se.inera.intyg.minaintyg.auth;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Value
@Builder
public class MinaIntygUser implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  Set<SimpleGrantedAuthority> roles = Collections.singleton(
      new SimpleGrantedAuthority("ROLE_ORGANIZATION_DELEGATE"));
  String personId;
  String personName;
  LoginMethod loginMethod;

  public MinaIntygUser(String personId, String personName, LoginMethod loginMethod) {
    this.personId = personId;
    this.personName = personName;
    this.loginMethod = loginMethod;
  }
}
