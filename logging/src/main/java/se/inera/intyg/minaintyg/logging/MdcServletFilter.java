package se.inera.intyg.minaintyg.logging;

import static se.inera.intyg.minaintyg.logging.MdcLogConstants.SESSION_ID_KEY;
import static se.inera.intyg.minaintyg.logging.MdcLogConstants.SPAN_ID_KEY;
import static se.inera.intyg.minaintyg.logging.MdcLogConstants.TRACE_ID_KEY;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Component
public class MdcServletFilter implements Filter {

  @Autowired
  private MdcHelper mdcHelper;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      if (request instanceof HttpServletRequest http) {
        MDC.put(SESSION_ID_KEY, mdcHelper.sessionId(http));
        MDC.put(TRACE_ID_KEY, mdcHelper.traceId(http));
        MDC.put(SPAN_ID_KEY, mdcHelper.spanId());
      }
      chain.doFilter(request, response);
    } finally {
      MDC.clear();
    }
  }

  @Override
  public void init(FilterConfig filterConfig) {
    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
        filterConfig.getServletContext());
  }
}
