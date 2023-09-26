package se.inera.intyg.minaintyg.testhelper;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;

public class TestPrincipalHelper {

  public static void setMinaIntygUserAsPrincipal(final MinaIntygUser user) {
    Authentication auth = new AbstractAuthenticationToken(null) {
      @Override
      public Object getCredentials() {
        return null;
      }

      @Override
      public Object getPrincipal() {
        return user;
      }
    };
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  public static void setUnknownPrincipal(final Object user) {
    Authentication auth = new AbstractAuthenticationToken(null) {
      @Override
      public Object getCredentials() {
        return null;
      }

      @Override
      public Object getPrincipal() {
        return user;
      }
    };
    SecurityContextHolder.getContext().setAuthentication(auth);
  }
}
