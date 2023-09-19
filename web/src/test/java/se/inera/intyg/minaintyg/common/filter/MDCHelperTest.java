package se.inera.intyg.minaintyg.common.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.inera.intyg.minaintyg.common.filter.MDCHelper.verifyRequestType;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MDCHelperTest {

  private MDCHelper mdcHelper;

  @BeforeEach
  void setUp() {
    mdcHelper = new MDCHelper();
  }

  @Nested
  class ValidateRequest {

    @Test
    void shouldReturnTrueIfTypeHttpServletRequest() {
      final var httpServletRequest = mock(HttpServletRequest.class);
      assertTrue(verifyRequestType(httpServletRequest));
    }

    @Test
    void shouldReturnFalseIfTypeNotHttpServletRequest() {
      final var servletRequest = mock(ServletRequest.class);
      assertFalse(verifyRequestType(servletRequest));
    }
  }

  @Nested
  class SessionId {

    @Test
    void shouldReturnSessionIdFromCookie() {
      final var expectedValue = "sessionId";
      final var httpServletRequest = mock(HttpServletRequest.class);
      when(httpServletRequest.getCookies()).thenReturn(
          new Cookie[]{new Cookie("SESSION", expectedValue)});
      final var result = mdcHelper.buildSessionInfo(httpServletRequest);
      assertEquals(expectedValue, result);
    }

    @Test
    void shouldReturnNullIfSessionIdFromCookieNotPresent() {
      final var httpServletRequest = mock(HttpServletRequest.class);
      when(httpServletRequest.getCookies()).thenReturn(
          new Cookie[]{new Cookie("NOTSESSION", "value")});
      final var result = mdcHelper.buildSessionInfo(httpServletRequest);
      assertNull(result);
    }
  }

  @Nested
  class TraceId {

    @Test
    void shouldReturnTraceIdFromHeader() {
      final var expectedValue = "sessionId";
      final var httpServletRequest = mock(HttpServletRequest.class);
      when(httpServletRequest.getHeader(any())).thenReturn(expectedValue);
      final var result = mdcHelper.buildTraceId(httpServletRequest);
      assertEquals(expectedValue, result);
    }

    @Test
    void shouldGenerateTraceIdIfNotPresentInHeader() {
      final var httpServletRequest = mock(HttpServletRequest.class);
      final var result = mdcHelper.buildTraceId(httpServletRequest);
      assertNotNull(result);
    }
  }
}
