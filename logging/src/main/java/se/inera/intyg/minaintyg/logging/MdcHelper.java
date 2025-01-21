package se.inera.intyg.minaintyg.logging;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.CharBuffer;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class MdcHelper {

  public static final String LOG_TRACE_ID_HEADER = "x-trace-id";
  public static final String LOG_SESSION_ID_HEADER = "x-session-id";
  private static final String SESSION_COOKIE_NAME = "SESSION";
  private static final int LENGTH_LIMIT = 8;
  private static final char[] BASE62CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

  public String sessionId(HttpServletRequest http) {
    return Optional.ofNullable(
            getSessionIdFromCookie(http)
        )
        .orElse("-");
  }

  public String traceId(HttpServletRequest http) {
    return Optional.ofNullable(
            http.getHeader(LOG_TRACE_ID_HEADER)
        )
        .orElse(generateId());
  }

  public String traceId() {
    return generateId();
  }

  public String spanId() {
    return generateId();
  }

  private String generateId() {
    final CharBuffer charBuffer = CharBuffer.allocate(LENGTH_LIMIT);
    IntStream.generate(() -> ThreadLocalRandom.current().nextInt(BASE62CHARS.length))
        .limit(LENGTH_LIMIT)
        .forEach(value -> charBuffer.append(BASE62CHARS[value]));
    return charBuffer.rewind().toString();
  }

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
}
