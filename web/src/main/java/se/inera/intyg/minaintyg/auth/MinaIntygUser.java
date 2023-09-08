package se.inera.intyg.minaintyg.auth;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class MinaIntygUser implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private final Set<SimpleGrantedAuthority> roles = Collections.singleton(
      new SimpleGrantedAuthority("ROLE_ORGANIZATION_DELEGATE"));
  private final String personId;
  private final String personName;

  public MinaIntygUser(String personId, String personName) {
    this.personId = personId;
    this.personName = personName;
  }

  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }

  public String getPersonId() {
    return this.personId;
  }

  public String getPersonName() {
    return personName;
  }
}
