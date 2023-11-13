package se.inera.intyg.minaintyg.auth;

import java.util.concurrent.TimeUnit;

public final class SessionConstants {

  public static final String SESSION_STATUS_REQUEST_MAPPING = "/api/session";
  public static final String SESSION_STATUS_PING = "/ping";
  
  public static final String SESSION_STATUS_CHECK_URI =
      SESSION_STATUS_REQUEST_MAPPING + SESSION_STATUS_PING;
  public static final String LAST_ACCESS_ATTRIBUTE = "LastAccess";
  public static final String SECONDS_UNTIL_EXPIRE = "SecondsUntilExpire";
  public static final Long SESSION_EXPIRATION_LIMIT = TimeUnit.MINUTES.toMillis(25);

  private SessionConstants() {
    throw new IllegalStateException("Utility class!");
  }
}
