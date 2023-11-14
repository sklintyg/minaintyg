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

  @InjectMocks
  private SessionController sessionController;

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