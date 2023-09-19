package se.inera.intyg.minaintyg.common.filter;

import static se.inera.intyg.minaintyg.integration.api.constants.MDCLogConstants.LOG_TRACE_ID_HEADER;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.CharBuffer;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class MDCHelper {

  private static final int LENGTH_LIMIT = 8;
  private static final String SESSION_COOKIE_NAME = "SESSION";

  private static final char[] BASE62CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

  private String getSessionIdFromCookie(HttpServletRequest http) {
    final var cookies = http.getCookies();
    if (cookies == null) {
      return null;
    }
    return Stream.of(cookies)
        .filter(cookie -> SESSION_COOKIE_NAME.equals(cookie.getName()))
        .map(Cookie::getValue)
        .findFirst().orElse(null);
  }

  private Optional<String> getTraceId(HttpServletRequest http) {
    if (http.getHeader(LOG_TRACE_ID_HEADER) == null) {
      return Optional.empty();
    }
    return Optional.of(http.getHeader(LOG_TRACE_ID_HEADER));
  }

  private String generateTraceId() {
    final CharBuffer charBuffer = CharBuffer.allocate(LENGTH_LIMIT);
    IntStream.generate(() -> ThreadLocalRandom.current().nextInt(BASE62CHARS.length))
        .limit(LENGTH_LIMIT)
        .forEach(value -> charBuffer.append(BASE62CHARS[value]));
    return charBuffer.rewind().toString();
  }

  public String buildSessionInfo(HttpServletRequest http) {
    return getSessionIdFromCookie(http);
  }

  public String buildTraceId(HttpServletRequest http) {
    return getTraceId(http).orElse(generateTraceId());
  }
}
