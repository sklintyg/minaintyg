package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

  @InjectMocks
  private SessionController sessionController;

  private static HttpServletRequest request;

  @Nested
  class WithSession {

    @BeforeEach
    void setup() {
      request = mock(HttpServletRequest.class);
      final var context = mock(SecurityContext.class);
      final var authentication = mock(Authentication.class);
      final var session = mock(HttpSession.class);

      when(request.getSession((false))).thenReturn(session);
      when(session.getAttribute(anyString())).thenReturn(context);
      when(context.getAuthentication()).thenReturn(authentication);
      when(authentication.getPrincipal()).thenReturn(MinaIntygUser.builder().build());
    }

    @Test
    void testGetSessionWhenSession() {
      final var sessionStatus = sessionController.getSessionStatus(request);

      assertTrue(sessionStatus.isHasSession());
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
    void testGetSessionStatusWhenNoSession() {
      final var sessionStatus = sessionController.getSessionStatus(request);

      assertFalse(sessionStatus.isHasSession());
    }
  }

}