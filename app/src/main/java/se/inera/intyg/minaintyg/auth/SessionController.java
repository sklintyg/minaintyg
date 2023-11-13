package se.inera.intyg.minaintyg.auth;

import static se.inera.intyg.minaintyg.auth.SessionConstants.SECONDS_UNTIL_EXPIRE;
import static se.inera.intyg.minaintyg.auth.SessionConstants.SESSION_STATUS_PING;
import static se.inera.intyg.minaintyg.auth.SessionConstants.SESSION_STATUS_REQUEST_MAPPING;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.auth.dto.SessionStatusResponseDTO;

@RestController
@RequestMapping(SESSION_STATUS_REQUEST_MAPPING)
public class SessionController {

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
