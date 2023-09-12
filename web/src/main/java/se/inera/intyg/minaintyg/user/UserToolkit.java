package se.inera.intyg.minaintyg.user;

import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;

public class UserToolkit {

  public static Optional<MinaIntygUser> getUserFromPrincipal() {
    final var auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null) {
      return Optional.empty();
    }

    final var principal = auth.getPrincipal();

    return getMinaIntygUser(principal);
  }

  private static Optional<MinaIntygUser> getMinaIntygUser(Object principal) {
    if (!(principal instanceof MinaIntygUser)) {
      return Optional.empty();
    }
    return Optional.of((MinaIntygUser) principal);
  }
}
