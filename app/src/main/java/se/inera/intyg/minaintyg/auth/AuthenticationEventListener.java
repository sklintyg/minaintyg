package se.inera.intyg.minaintyg.auth;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import se.inera.intyg.minaintyg.logging.MdcLogConstants;
import se.inera.intyg.minaintyg.logging.service.MonitoringLogService;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationEventListener {

  private final MonitoringLogService monitoringLogService;

  @EventListener
  public void onLoginSuccess(InteractiveAuthenticationSuccessEvent success) {
    updateMDCWithNewSessionId();

    final var minaIntygUser = getMinaIntygUser(success.getAuthentication().getPrincipal());
    minaIntygUser.ifPresent(user ->
        monitoringLogService.logUserLogin(
            user.getUserId(),
            user.getLoginMethod().value()
        )
    );
  }

  /**
   * Spring Security will by default invalidate the old session and create a new one after
   * authentication. It’s a security feature to protect against session fixation attacks. Update the
   * MDC with the new session id.
   */
  private static void updateMDCWithNewSessionId() {
    final var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attrs != null) {
      final var request = attrs.getRequest();
      final var session = request.getSession(false);

      if (session != null && session.getId() != null) {
        final var sessionId = session.getId();
        final var encodedSessionId = Base64.getEncoder().encodeToString(sessionId.getBytes(
            StandardCharsets.UTF_8));
        MDC.put(MdcLogConstants.SESSION_ID_KEY, encodedSessionId);
      }
    }
  }

  @EventListener
  public void onLogoutSuccess(LogoutSuccessEvent success) {
    final var minaIntygUser = getMinaIntygUser(success.getAuthentication().getPrincipal());
    minaIntygUser.ifPresent(user ->
        monitoringLogService.logUserLogout(
            user.getUserId(),
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


