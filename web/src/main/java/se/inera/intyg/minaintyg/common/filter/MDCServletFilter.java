package se.inera.intyg.minaintyg.common.filter;


import static se.inera.intyg.minaintyg.common.filter.MDCHelper.verifyRequestType;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MDCServletFilter implements Filter {

  private final MDCHelper mdcHelper;
  @Value("${mdc.session.info.key}")
  private String sessionInfoKey;
  @Value("${mdc.trace.id.key}")
  private String traceIdKey;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (verifyRequestType(request)) {
      final var http = (HttpServletRequest) request;
      MDC.put(sessionInfoKey, mdcHelper.buildSessionInfo(http));
      MDC.put(traceIdKey, mdcHelper.buildTraceId(http));
    }
    try {
      chain.doFilter(request, response);
    } finally {
      MDC.clear();
    }
  }
}
