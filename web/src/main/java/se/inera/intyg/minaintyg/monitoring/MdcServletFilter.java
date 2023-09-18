package se.inera.intyg.minaintyg.monitoring;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Stream;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MdcServletFilter implements Filter {

  @Value("${log.trace.header}")
  private String traceIdHeader;
  @Value("${mdc.session.info.key}")
  private String sessionInfoKey;
  @Value("${mdc.trace.id.key}")
  private String traceIdKey;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (request instanceof final HttpServletRequest http) {
      MDC.put(sessionInfoKey, getSessionIdFromCookie(http));
      MDC.put(traceIdKey, http.getHeader(traceIdHeader));
    }
    try {
      chain.doFilter(request, response);
    } finally {
      MDC.clear();
    }
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
}
