package se.inera.intyg.minaintyg.auth;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.common.logging.MonitoringLogService;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationEventListener {

  private final MonitoringLogService monitoringLogService;

  @EventListener
  public void onLoginSuccess(InteractiveAuthenticationSuccessEvent success) {
    final var minaIntygUser = getMinaIntygUser(success.getAuthentication().getPrincipal());
    minaIntygUser.ifPresent(user ->
        monitoringLogService.logUserLogin(
            user.getPersonId(),
            user.getLoginMethod().value()
        )
    );
  }

  @EventListener
  public void onLogoutSuccess(LogoutSuccessEvent success) {
    final var minaIntygUser = getMinaIntygUser(success.getAuthentication().getPrincipal());
    minaIntygUser.ifPresent(user ->
        monitoringLogService.logUserLogout(
            user.getPersonId(),
            user.getLoginMethod().value()
        )
    );
  }

  private static Optional<MinaIntygUser> getMinaIntygUser(Object principal) {
    if (principal instanceof MinaIntygUser minaIntygUser) {
      return Optional.of(minaIntygUser);
    }
    log.warn("Invalid principal [" + principal.getClass().getSimpleName() + "]");
    return Optional.empty();
  }
}


