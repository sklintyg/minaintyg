package se.inera.intyg.minaintyg.auth;

import static se.inera.intyg.minaintyg.auth.SessionTimeoutService.SECONDS_UNTIL_EXPIRE;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.auth.dto.SessionStatusResponseDTO;

@RestController
@RequestMapping("/api/session-auth-check")
public class SessionController {

  private static final String SESSION_STATUS_REQUEST_MAPPING = "/api/session-auth-check";
  private static final String SESSION_STATUS_PING = "/ping";

  public static final String SESSION_STATUS_CHECK_URI =
      SESSION_STATUS_REQUEST_MAPPING + SESSION_STATUS_PING;

  @GetMapping(SESSION_STATUS_PING)
  SessionStatusResponseDTO getSessionStatus(HttpServletRequest request) {
    return createSessionStatusResponse(request);
  }

  private SessionStatusResponseDTO createSessionStatusResponse(HttpServletRequest request) {
    final var session = request.getSession(false);
    final var secondsLeft = request.getAttribute(SECONDS_UNTIL_EXPIRE);

    return SessionStatusResponseDTO.builder()
        .hasSession(session != null)
        .secondsUntilExpire(secondsLeft == null ? 0 : (Long) secondsLeft)
        .build();
  }

}
