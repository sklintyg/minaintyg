package se.inera.intyg.minaintyg.auth;

import java.io.Serial;
import java.util.Collections;
import java.util.Objects;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class FakeAuthenticationToken extends AbstractAuthenticationToken {

  @Serial
  private static final long serialVersionUID = 1L;

  private final MinaIntygUser minaIntygUser;

  public FakeAuthenticationToken(MinaIntygUser minaIntygUser) {
    super(Collections.emptyList());
    this.minaIntygUser = minaIntygUser;
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return minaIntygUser.getPersonId();
  }

  @Override
  public Object getPrincipal() {
    return minaIntygUser;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    final var that = (FakeAuthenticationToken) o;
    return Objects.equals(minaIntygUser, that.minaIntygUser);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), minaIntygUser);
  }
}
