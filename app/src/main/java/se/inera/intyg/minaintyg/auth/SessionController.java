/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
    if (session == null) {
      return SessionStatusResponseDTO.builder().build();
    }

    final var secondsLeft = session.getAttribute(SECONDS_UNTIL_EXPIRE);
    return SessionStatusResponseDTO.builder()
        .hasSession(true)
        .secondsUntilExpire(secondsLeft == null ? 0 : (Long) secondsLeft)
        .build();
  }
}
