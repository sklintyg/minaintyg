package se.inera.intyg.minaintyg.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(-101) // To run before FilterChainProxy
public class SamlExtensionUrlForwardingFilter extends OncePerRequestFilter {

  private static final Map<String, String> urlMapping = Map.of(
      "/saml/SSO", "/login/saml2/sso/eleg",
      "/saml/login", "/saml2/authenticate/eleg",
      "/saml/logout", "/logout/saml2/slo",
      "/saml/SingleLogout", "/logout/saml2/slo",
      "/saml/metadata", "/saml2/service-provider-metadata/one");

  private final RequestMatcher matcher = createRequestMatcher();

  private RequestMatcher createRequestMatcher() {
    Set<String> urls = urlMapping.keySet();
    List<RequestMatcher> matchers = new LinkedList<>();
    urls.forEach((url) -> matchers.add(new AntPathRequestMatcher(url)));
    return new OrRequestMatcher(matchers);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    final var match = this.matcher.matches(request);
    if (!match) {
      filterChain.doFilter(request, response);
      return;
    }
    final var forwardUrl = urlMapping.get(request.getRequestURI());
    final var dispatcher = request.getRequestDispatcher(forwardUrl);
    dispatcher.forward(request, response);
  }
}
