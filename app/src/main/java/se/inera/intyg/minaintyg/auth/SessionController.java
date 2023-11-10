package se.inera.intyg.minaintyg.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.auth.dto.SessionStatusResponseDTO;

@RestController
@RequestMapping("/api/session-auth-check")
public class SessionController {

  private static final String SESSION_STATUS_REQUEST_MAPPING = "/api/session-auth-check";
  private static final String SESSION_STATUS_PING = "/ping";
  private static final String SESSION_STATUS_EXTEND = "/extend";

  public static final String SESSION_STATUS_CHECK_URI =
      SESSION_STATUS_REQUEST_MAPPING + SESSION_STATUS_PING;

  @GetMapping(SESSION_STATUS_PING)
  SessionStatusResponseDTO getSessionStatus(HttpServletRequest request) {
    return createSessionStatusResponse(request);
  }

  @GetMapping(SESSION_STATUS_EXTEND)
  SessionStatusResponseDTO extendSession(HttpServletRequest request) {
    return createSessionStatusResponse(request);
  }

  private SessionStatusResponseDTO createSessionStatusResponse(HttpServletRequest request) {
    final var session = request.getSession(false);
    final var secondsLeft = request.getAttribute(
        SessionTimeoutFilter.SECONDS_UNTIL_SESSION_EXPIRE_ATTRIBUTE_KEY);

    return SessionStatusResponseDTO.builder()
        .hasSession(session != null)
        .isAuthenticated(hasAuthenticatedPrincipalSession(session))
        .secondsUntilExpire(secondsLeft == null ? 0 : (Long) secondsLeft)
        .build();
  }

  private boolean hasAuthenticatedPrincipalSession(HttpSession session) {
    if (session != null) {
      final var context = session.getAttribute(
          HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
      if (context instanceof SecurityContext securityContext) {
        return securityContext.getAuthentication() != null
            && securityContext.getAuthentication().getPrincipal() != null;
      }

    }
    return false;
  }

}
