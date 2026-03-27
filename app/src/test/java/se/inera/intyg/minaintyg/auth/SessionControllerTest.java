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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

  @InjectMocks private SessionController sessionController;

  private static HttpServletRequest request;

  @Nested
  class WithSession {

    private static final Long SECONDS_UNTIL_EXPIRE = 10L;

    @BeforeEach
    void setup() {
      request = mock(HttpServletRequest.class);
      final var session = new MockHttpSession();
      session.setAttribute(SessionConstants.SECONDS_UNTIL_EXPIRE, SECONDS_UNTIL_EXPIRE);

      when(request.getSession(false)).thenReturn(session);
    }

    @Test
    void shallReturnHasSessionTrue() {
      final var sessionStatus = sessionController.getSessionStatus(request);

      assertEquals(Boolean.TRUE, sessionStatus.isHasSession());
    }

    @Test
    void shallReturnSecondsUntilExpire() {
      final var sessionStatus = sessionController.getSessionStatus(request);

      assertEquals(SECONDS_UNTIL_EXPIRE, sessionStatus.getSecondsUntilExpire());
    }
  }

  @Nested
  class NoSession {

    @BeforeEach
    void setup() {
      request = mock(HttpServletRequest.class);
      when(request.getSession((false))).thenReturn(null);
    }

    @Test
    void shallReturnHasSessionFalse() {
      final var sessionStatus = sessionController.getSessionStatus(request);

      assertEquals(Boolean.FALSE, sessionStatus.isHasSession());
    }

    @Test
    void shallReturnSecondsUntilExpireAsZero() {
      final var sessionStatus = sessionController.getSessionStatus(request);

      assertEquals(0, sessionStatus.getSecondsUntilExpire());
    }
  }
}
