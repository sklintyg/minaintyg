package se.inera.intyg.minaintyg.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SessionTimeoutService {

  public static final String LAST_ACCESS_ATTRIBUTE = "LastAccess";
  public static final String SECONDS_UNTIL_EXPIRE = "SecondsUntilExpire";
  public static final Long SESSION_EXPIRATION_LIMIT = TimeUnit.MINUTES.toMillis(25);


  public void checkSessionValidity(HttpServletRequest request, List<String> excludedUrls) {
    final var session = request.getSession(false);

    if (isSessionExpired(session)) {
      log.info("Invalidating session due to inactivity.");
      session.invalidate();
      return;
    }

    updateSession(session, request, excludedUrls);
  }

  private static void updateSession(
      HttpSession session,
      HttpServletRequest request,
      List<String> excludedUrls) {
    if (isExcludedURL(request, excludedUrls)) {
      session.setAttribute(SECONDS_UNTIL_EXPIRE, getExpirationTime(session));
      return;
    }

    session.setAttribute(SECONDS_UNTIL_EXPIRE, getSeconds(SESSION_EXPIRATION_LIMIT));
    session.setAttribute(LAST_ACCESS_ATTRIBUTE, System.currentTimeMillis());
  }

  private static Long getSeconds(Long ms) {
    return TimeUnit.MILLISECONDS.toSeconds(ms);
  }

  private static boolean isExcludedURL(HttpServletRequest request, List<String> excludedUrls) {
    return excludedUrls.stream().anyMatch(url -> url.equals(request.getRequestURI()));
  }

  private static Long getLastAccessedTime(HttpSession session) {
    return session.getAttribute(LAST_ACCESS_ATTRIBUTE) != null
        ? (Long) session.getAttribute(LAST_ACCESS_ATTRIBUTE) : 0;
  }

  private static Long getExpirationTime(HttpSession session) {
    final var inactiveTime =
        System.currentTimeMillis() - getLastAccessedTime(session);
    return getSeconds(SESSION_EXPIRATION_LIMIT - inactiveTime);
  }

  private static boolean isSessionExpired(HttpSession session) {
    final var inactiveTime = System.currentTimeMillis() - getLastAccessedTime(session);
    return inactiveTime > SESSION_EXPIRATION_LIMIT;
  }

}
