package se.inera.intyg.minaintyg.auth;

public final class SessionConstants {

  public static final String SESSION_STATUS_REQUEST_MAPPING = "/api/session";
  public static final String SESSION_STATUS_PING = "/ping";

  public static final String SESSION_STATUS_CHECK_URI =
      SESSION_STATUS_REQUEST_MAPPING + SESSION_STATUS_PING;

  private SessionConstants() {
    throw new IllegalStateException("Utility class!");
  }
}
