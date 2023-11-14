package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.minaintyg.auth.SessionConstants.LAST_ACCESS_ATTRIBUTE;
import static se.inera.intyg.minaintyg.auth.SessionConstants.SECONDS_UNTIL_EXPIRE;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class SessionTimeoutServiceTest {

  private static final String ACTUAL_URL = "localhost/api/ping";
  private static final List<String> EXCLUDED_URLS = List.of("url");
  private static final List<String> EXCLUDED_ACTUAL_URLS = List.of(ACTUAL_URL);
  public static final int HUNDRED_SECONDS = 100;
  public static final long LAST_ACCESS_TIME = System.currentTimeMillis();
  public static final int TIMEOUT_LIMIT_IN_MINUTES = 25;
  public static final long TIMEOUT_LIMIT_IN_SECONDS = TimeUnit.MINUTES.toSeconds(
      TIMEOUT_LIMIT_IN_MINUTES);

  HttpServletRequest request;
  MockHttpSession session = new MockHttpSession();

  @InjectMocks
  private SessionTimeoutService sessionTimeoutService;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(sessionTimeoutService, "timeoutLimitInMinutes",
        TIMEOUT_LIMIT_IN_MINUTES);
    ReflectionTestUtils.setField(sessionTimeoutService, "excludedUrls", EXCLUDED_URLS);
  }

  @Nested
  class TestNoSession {

    @BeforeEach
    void setup() {
      request = mock(HttpServletRequest.class);
    }

    @Test
    void shouldHandleIfNoSession() {
      sessionTimeoutService.checkSessionValidity(request);
      verify(request).getSession(false);
    }
  }

  @Nested
  class TestNewSession {

    @BeforeEach
    void setup() {
      request = mock(HttpServletRequest.class);

      when(request.getSession(anyBoolean())).thenReturn(session);
      when(request.getRequestURI()).thenReturn(ACTUAL_URL);
    }

    @Test
    void shouldSetLastAccessAttributeToNowWhenCheckingValidityForTheFirstTime() {
      sessionTimeoutService.checkSessionValidity(request);
      assertEquals(
          System.currentTimeMillis(), (Long) session.getAttribute(LAST_ACCESS_ATTRIBUTE), 100
      );
    }

    @Test
    void shouldSetTimeToExpireToInitialValue() {
      sessionTimeoutService.checkSessionValidity(request);
      assertEquals(TimeUnit.MINUTES.toSeconds(TIMEOUT_LIMIT_IN_MINUTES),
          (Long) session.getAttribute(SECONDS_UNTIL_EXPIRE));
    }
  }

  @Nested
  class TestInvalidatedSession {

    @BeforeEach
    void setup() {
      request = mock(HttpServletRequest.class);

      when(request.getSession(anyBoolean())).thenReturn(session);
      session.setAttribute(LAST_ACCESS_ATTRIBUTE,
          System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(TIMEOUT_LIMIT_IN_MINUTES) - 1000
      );
      session.setAttribute(SECONDS_UNTIL_EXPIRE, 0L);
    }

    @Test
    void shouldInvalidateSessionIfExpiredIncludedURL() {
      sessionTimeoutService.checkSessionValidity(request);

      assertTrue(session.isInvalid());
    }

    @Test
    void shouldInvalidateSessionIfExpiredExcludedURL() {
      sessionTimeoutService.checkSessionValidity(request);

      assertTrue(session.isInvalid());
    }
  }

  @Nested
  class TestValidSession {

    @BeforeEach
    void setup() {
      request = mock(HttpServletRequest.class);

      when(request.getSession(anyBoolean())).thenReturn(session);
      session.setAttribute(LAST_ACCESS_ATTRIBUTE, LAST_ACCESS_TIME);
      session.setAttribute(SECONDS_UNTIL_EXPIRE, 600);

      when(request.getRequestURI()).thenReturn(ACTUAL_URL);
    }

    @Test
    void shouldNotCreateNewSessionWhenCallingGetSession() {
      final var captor = ArgumentCaptor.forClass(Boolean.class);
      sessionTimeoutService.checkSessionValidity(request);

      verify(request).getSession(captor.capture());
      assertFalse(captor.getValue());
    }

    @Test
    void shouldResetLastAccessedTimeIfIncludedUrl() {
      sessionTimeoutService.checkSessionValidity(request);

      assertEquals(System.currentTimeMillis(), (Long) session.getAttribute(LAST_ACCESS_ATTRIBUTE),
          100);
      assertNotEquals(LAST_ACCESS_TIME, (Long) session.getAttribute(LAST_ACCESS_ATTRIBUTE));
    }

    @Test
    void shouldNotResetLastAccessedTimeIfExcludedUrl() {
      ReflectionTestUtils.setField(sessionTimeoutService, "excludedUrls", EXCLUDED_ACTUAL_URLS);

      sessionTimeoutService.checkSessionValidity(request);

      assertEquals(LAST_ACCESS_TIME, session.getAttribute(LAST_ACCESS_ATTRIBUTE));
    }

    @Test
    void shouldNotResetLastAccessedTimeIfUrlContainsExcludedUrl() {
      ReflectionTestUtils.setField(sessionTimeoutService, "excludedUrls", EXCLUDED_ACTUAL_URLS);

      sessionTimeoutService.checkSessionValidity(request);

      assertEquals(LAST_ACCESS_TIME, session.getAttribute(LAST_ACCESS_ATTRIBUTE));
    }

    @Test
    void shouldNotInvalidateSessionIfNotExpiredIncludedURL() {
      sessionTimeoutService.checkSessionValidity(request);

      assertFalse(session.isInvalid());
    }

    @Test
    void shouldNotInvalidateSessionIfNotExpiredExcludedURL() {
      sessionTimeoutService.checkSessionValidity(request);

      assertFalse(session.isInvalid());
    }

    @Test
    void shouldUpdateSecondsUntilExpireToTimePassedSinceLastAccess() {
      final var expectedSecondsUntilExpire = TIMEOUT_LIMIT_IN_SECONDS - HUNDRED_SECONDS;

      session.setAttribute(
          LAST_ACCESS_ATTRIBUTE, LAST_ACCESS_TIME - TimeUnit.SECONDS.toMillis(HUNDRED_SECONDS)
      );
      ReflectionTestUtils.setField(sessionTimeoutService, "excludedUrls", EXCLUDED_ACTUAL_URLS);

      sessionTimeoutService.checkSessionValidity(request);

      assertEquals(expectedSecondsUntilExpire, (Long) session.getAttribute(SECONDS_UNTIL_EXPIRE),
          5);
    }

    @Test
    void shouldSetSecondsUntilExpireForIncludedUrl() {
      sessionTimeoutService.checkSessionValidity(request);

      assertEquals(TimeUnit.MINUTES.toSeconds(TIMEOUT_LIMIT_IN_MINUTES),
          (Long) session.getAttribute(SECONDS_UNTIL_EXPIRE));
    }
  }
}