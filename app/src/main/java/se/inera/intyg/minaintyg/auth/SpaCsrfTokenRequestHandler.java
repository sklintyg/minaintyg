package se.inera.intyg.minaintyg.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.function.Supplier;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.util.StringUtils;

/**
 * This handler is required to make CSRF-protection work in a single-page application.
 * <p>
 * <a
 * href="https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-spa">Spring
 * Security - Single-Page Applications</a>
 */
public final class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler {

  /**
   * Use CsrfTokenRequestAttributeHandler instead of XorCsrfTokenRequestAttributeHandler. The latter
   * decodes the csrf-token in a way that doesn't work when we generate the form in our SPA instead
   * of serverside. Keeping the delegate to simplify changes later.
   */
  private final CsrfTokenRequestHandler delegate = new CsrfTokenRequestAttributeHandler();

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      Supplier<CsrfToken> csrfToken) {
    this.delegate.handle(request, response, csrfToken);
  }

  @Override
  public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
    /*
     * If the request contains a request header, use CsrfTokenRequestAttributeHandler
     * to resolve the CsrfToken. This applies when a single-page application includes
     * the header value automatically, which was obtained via a cookie containing the
     * raw CsrfToken.
     */
    if (StringUtils.hasText(request.getHeader(csrfToken.getHeaderName()))) {
      return super.resolveCsrfTokenValue(request, csrfToken);
    }
    /*
     * In all other cases (e.g. if the request contains a request parameter), use
     * XorCsrfTokenRequestAttributeHandler to resolve the CsrfToken. This applies
     * when a server-side rendered form includes the _csrf request parameter as a
     * hidden input.
     *
     * NOTE: Because we don't do server-side rendering of the form, we cannot use
     * the XorCsrfTokenRequestAttributeHandler.
     */
    return this.delegate.resolveCsrfTokenValue(request, csrfToken);
  }
}
