package se.inera.intyg.minaintyg.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.inera.intyg.minaintyg.logging.MdcHelper.LOG_TRACE_ID_HEADER;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MdcHelperTest {

  private MdcHelper mdcHelper;

  @BeforeEach
  void setUp() {
    mdcHelper = new MdcHelper();
  }

  @Nested
  class SessionId {

    @Test
    void shouldReturnSessionIdFromCookie() {
      final var expectedValue = "sessionId";
      final var httpServletRequest = mock(HttpServletRequest.class);
      when(httpServletRequest.getCookies()).thenReturn(
          new Cookie[]{new Cookie("SESSION", expectedValue)});
      final var result = mdcHelper.sessionId(httpServletRequest);
      assertEquals(expectedValue, result);
    }

    @Test
    void shouldReturnEmptySessionIdIfNotPresentInHeader() {
      final var expectedValue = "-";
      final var httpServletRequest = mock(HttpServletRequest.class);
      final var result = mdcHelper.sessionId(httpServletRequest);
      assertEquals(expectedValue, result);
    }
  }

  @Nested
  class TraceId {

    @Test
    void shouldReturnTraceIdFromHeader() {
      final var expectedValue = "traceId";
      final var httpServletRequest = mock(HttpServletRequest.class);
      when(httpServletRequest.getHeader(LOG_TRACE_ID_HEADER)).thenReturn(expectedValue);
      final var result = mdcHelper.traceId(httpServletRequest);
      assertEquals(expectedValue, result);
    }

    @Test
    void shouldGenerateTraceIdIfNotPresentInHeader() {
      final var httpServletRequest = mock(HttpServletRequest.class);
      final var result = mdcHelper.traceId(httpServletRequest);
      assertNotNull(result);
    }

    @Test
    void shouldGeneratetraceId() {
      final var result = mdcHelper.traceId();
      assertNotNull(result);
    }
  }

  @Nested
  class SpanId {

    @Test
    void shouldGenerateSpanId() {
      final var result = mdcHelper.spanId();
      assertNotNull(result);
    }
  }
}
