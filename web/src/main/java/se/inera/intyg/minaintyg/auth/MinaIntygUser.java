package se.inera.intyg.minaintyg.auth;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class MinaIntygUser implements User {

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

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return null;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String getPersonId() {
    return this.personId;
  }

  public String getPersonName() {
    return personName;
  }
}
