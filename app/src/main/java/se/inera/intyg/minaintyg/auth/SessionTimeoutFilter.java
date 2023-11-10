package se.inera.intyg.minaintyg.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class SessionTimeoutFilter extends OncePerRequestFilter {

  public static final String TIME_TO_INVALIDATE_ATTRIBUTE_NAME =
      SessionTimeoutFilter.class.getName() + ".SessionTimeToInvalidate";
  public static final String SECONDS_UNTIL_SESSION_EXPIRE_ATTRIBUTE_KEY =
      SessionTimeoutFilter.class.getName() + ".secondsToLive";

  private static final Logger LOG = LoggerFactory.getLogger(SessionTimeoutFilter.class);

  static final String LAST_ACCESS_TIME_ATTRIBUTE_NAME =
      SessionTimeoutFilter.class.getName() + ".SessionLastAccessTime";

  private static final long MILLISECONDS_PER_SECONDS = 1000;

  @Getter
  @Setter
  private String skipRenewSessionUrls;

  @Setter
  private List<String> skipRenewSessionUrlsList;

  @Override
  protected void initFilterBean() throws ServletException {
    super.initFilterBean();
    if (skipRenewSessionUrls == null) {
      LOG.warn("No skipRenewSessionUrls are configured!");
      skipRenewSessionUrlsList = new ArrayList<>();
    } else {
      skipRenewSessionUrlsList = Arrays.asList(skipRenewSessionUrls.split(","));
      final var urls = formatUrls(skipRenewSessionUrlsList);
      LOG.info("Configured skipRenewSessionUrls as: '{}'", urls);
    }
  }

  private String formatUrls(List<String> urls) {
    return urls.stream()
        .map(Object::toString)
        .collect(Collectors.joining(", "));
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    checkSessionValidity(request);

    filterChain.doFilter(request, response);
  }

  private void checkSessionValidity(HttpServletRequest request) {
    // Get existing session - if any
    HttpSession session = request.getSession(false);

    // Is it a request that should'nt prolong the expiration?
    String url = request.getRequestURI();
    boolean skipSessionUpdate =
        skipRenewSessionUrlsList.stream().anyMatch(url::contains);

    if (session != null) {
      Long lastAccess = (Long) session.getAttribute(LAST_ACCESS_TIME_ATTRIBUTE_NAME);

      if (invalidateSessionIfTimeToInvalidateHasPassed(session)) {
        return;
      }

      // Set an request attribute that other parties further down the request chaing can use.
      Long msUntilExpire = updateTimeLeft(request, session);

      if (msUntilExpire <= 0) {
        LOG.info("Session expired '{}' ms ago. Invalidating it now!", msUntilExpire);
        session.invalidate();
      } else if (!skipSessionUpdate || lastAccess == null) {
        // Update lastaccessed for ALL requests except status requests
        session.setAttribute(LAST_ACCESS_TIME_ATTRIBUTE_NAME, System.currentTimeMillis());
        updateTimeLeft(request, session);
      }
    }
  }

  private boolean invalidateSessionIfTimeToInvalidateHasPassed(HttpSession session) {
    final var invalidTime = (Long) session.getAttribute(TIME_TO_INVALIDATE_ATTRIBUTE_NAME);
    if (invalidTime == null) {
      return false;
    }

    final var currentTime = Instant.now().toEpochMilli();
    if (currentTime < invalidTime) {
      LOG.info("Current time {} is before invalid time {} - The session remains valid!",
          currentTime, invalidTime);
      return false;
    }

    LOG.info("Current time {} is past invalid time {} - The session will be invalidated!",
        currentTime, invalidTime);
    session.invalidate();
    return true;
  }

  private Long updateTimeLeft(HttpServletRequest request, HttpSession session) {
    Long lastAccess = (Long) session.getAttribute(LAST_ACCESS_TIME_ATTRIBUTE_NAME);
    long inactiveTime = (lastAccess == null) ? 0 : (System.currentTimeMillis() - lastAccess);
    long maxInactiveTime = session.getMaxInactiveInterval() * MILLISECONDS_PER_SECONDS;

    long msUntilExpire = maxInactiveTime - inactiveTime;
    request.setAttribute(SECONDS_UNTIL_SESSION_EXPIRE_ATTRIBUTE_KEY,
        msUntilExpire / MILLISECONDS_PER_SECONDS);
    return msUntilExpire;
  }
}
