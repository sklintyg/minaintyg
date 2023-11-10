package se.inera.intyg.minaintyg.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.minaintyg.auth.SessionTimeoutFilter.TIME_TO_INVALIDATE_ATTRIBUTE_NAME;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SessionTimeoutFilterTest {

  private static final String SKIP_RENEW_URL = "/test";
  private static final String OTHER_URL = "/any.html";
  private static final int FIVE_SECONDS_AGO = 5000;
  private static final int ONE_SECOND = 1;
  private static final int HALF_AN_HOUR = 1800;

  private SessionTimeoutFilter filter;

  @Mock
  HttpServletRequest request;
  @Mock
  HttpServletResponse response;
  @Mock
  FilterChain filterChain;
  @Mock
  HttpSession session;

  @BeforeEach
  void setupFilter() throws Exception {
    filter = new SessionTimeoutFilter();
    filter.setSkipRenewSessionUrls(SKIP_RENEW_URL);
    filter.initFilterBean();
  }

  @Nested
  class InvalidSession {

    @BeforeEach
    void setup() {
      when(session.getMaxInactiveInterval()).thenReturn(ONE_SECOND);
    }

    @Test
    void shouldFilterInvalidSession() throws Exception {
      setupMocks(OTHER_URL);

      filter.doFilterInternal(request, response, filterChain);

      verify(filterChain).doFilter(request, response);
      verify(session).invalidate();
      verify(session, never()).setAttribute(any(), any());
    }

    @Test
    void shouldFilterInvalidSessionWithSkipUrl() throws Exception {
      setupMocks(SKIP_RENEW_URL);

      filter.doFilterInternal(request, response, filterChain);

      verify(filterChain).doFilter(request, response);
      verify(session).invalidate();
      verify(session, never()).setAttribute(any(), any());
    }
  }

  @Nested
  class ValidSession {

    @BeforeEach
    void setup() {
      when(session.getMaxInactiveInterval()).thenReturn(HALF_AN_HOUR);
    }

    @Test
    void shouldFilterValidSession() throws Exception {
      setupMocks(OTHER_URL);

      filter.doFilterInternal(request, response, filterChain);

      verify(filterChain).doFilter(request, response);
      verify(session, never()).invalidate();
      verify(session).setAttribute(eq(SessionTimeoutFilter.LAST_ACCESS_TIME_ATTRIBUTE_NAME), any());

    }

    @Test
    void shouldFilterValidSessionWithSkipUrl() throws Exception {
      setupMocks(SKIP_RENEW_URL);

      filter.doFilterInternal(request, response, filterChain);

      verify(filterChain).doFilter(request, response);
      verify(session, never()).invalidate();
      verify(session, never()).setAttribute(any(), any());

    }

    @Test
    void shouldNotInvalidateSessionIfTimeToInvalidateHasNotPassed() throws Exception {
      setupMocksWithTimeToInvalidate(Instant.now().plusSeconds(2).toEpochMilli());

      filter.doFilterInternal(request, response, filterChain);

      verify(filterChain).doFilter(request, response);
      verify(session, never()).invalidate();
      verify(session, never()).setAttribute(any(), any());
    }

    @Test
    void shouldNotInvalidateSessionIfTimeToInvalidateIsNull() throws Exception {
      setupMocksWithTimeToInvalidate(null);

      filter.doFilterInternal(request, response, filterChain);

      verify(filterChain).doFilter(request, response);
      verify(session, never()).invalidate();
      verify(session, never()).setAttribute(any(), any());
    }
  }

  @Test
  void shouldInvalidateSessionIfTimeToInvalidateHasPassed() throws Exception {
    setupMocksWithTimeToInvalidate(Instant.now().minusSeconds(1).toEpochMilli());

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    verify(session, times(1)).invalidate();
    verify(session, never()).setAttribute(any(), any());
  }

  private void setupMocksWithTimeToInvalidate(Long timeToInvalidate) {
    setupMocks(SessionTimeoutFilterTest.SKIP_RENEW_URL);
    doReturn(timeToInvalidate).when(session).getAttribute(TIME_TO_INVALIDATE_ATTRIBUTE_NAME);
  }

  private void setupMocks(String reportedRequestURI) {
    when(request.getSession(false)).thenReturn(session);
    when(request.getRequestURI()).thenReturn(reportedRequestURI);
    when(session.getAttribute(SessionTimeoutFilter.LAST_ACCESS_TIME_ATTRIBUTE_NAME))
        .thenReturn(System.currentTimeMillis() - FIVE_SECONDS_AGO);
  }

}