package se.inera.intyg.minaintyg.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class SessionTimeoutFilter extends OncePerRequestFilter {

  private final SessionTimeoutService sessionTimeoutService;

  @Getter
  @Setter
  private List<String> skipRenewSessionUrls;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    sessionTimeoutService.checkSessionValidity(request, skipRenewSessionUrls);

    filterChain.doFilter(request, response);
  }
}
