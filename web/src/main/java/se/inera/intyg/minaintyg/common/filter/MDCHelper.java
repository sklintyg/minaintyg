package se.inera.intyg.minaintyg.common.filter;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.CharBuffer;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MDCHelper {

  @Value("${log.trace.header}")
  private String traceIdHeader;
  private static final int LENGHT_LIMIT = 8;

  private static final char[] BASE62CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

  public static boolean verifyRequestType(ServletRequest request) {
    return request instanceof HttpServletRequest;
  }

  private String getSessionIdFromCookie(HttpServletRequest http) {
    final var cookies = http.getCookies();
    if (cookies == null) {
      return null;
    }
    return Stream.of(cookies)
        .filter(cookie -> "SESSION".equals(cookie.getName()))
        .map(Cookie::getValue)
        .findFirst().orElse(null);
  }

  private Optional<String> getTraceId(HttpServletRequest http) {
    if (http.getHeader(traceIdHeader) == null) {
      return Optional.empty();
    }
    return Optional.of(http.getHeader(traceIdHeader));
  }

  private String generateTraceId() {
    final CharBuffer charBuffer = CharBuffer.allocate(LENGHT_LIMIT);
    IntStream.generate(() -> ThreadLocalRandom.current().nextInt(BASE62CHARS.length))
        .limit(LENGHT_LIMIT)
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
