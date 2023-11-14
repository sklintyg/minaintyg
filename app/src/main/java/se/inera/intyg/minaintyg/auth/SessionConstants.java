package se.inera.intyg.minaintyg.auth;

public final class SessionConstants {

  public static final String SESSION_STATUS_REQUEST_MAPPING = "/api/session";
  public static final String SESSION_STATUS_PING = "/ping";
  public static final String LAST_ACCESS_ATTRIBUTE = "LastAccess";
  public static final String SECONDS_UNTIL_EXPIRE = "SecondsUntilExpire";

  private SessionConstants() {
    throw new IllegalStateException("Utility class!");
  }
}
