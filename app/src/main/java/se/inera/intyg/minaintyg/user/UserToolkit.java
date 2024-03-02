package se.inera.intyg.minaintyg.user;

import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;

public class UserToolkit {

  private UserToolkit() {

  }

  public static Optional<MinaIntygUser> getUserFromPrincipal() {
    final var auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null) {
      return Optional.empty();
    }

    final var principal = auth.getPrincipal();

    return (principal instanceof MinaIntygUser minaIntygUser) ? Optional.of(minaIntygUser)
        : Optional.empty();
  }
}
