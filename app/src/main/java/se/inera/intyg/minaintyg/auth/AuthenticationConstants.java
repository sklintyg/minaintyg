package se.inera.intyg.minaintyg.auth;

public final class AuthenticationConstants {

  public static final String USER_ID_ATTRIBUTE = "Subject_SerialNumber";
  public static final String ELEG_PARTY_REGISTRATION_ID = "eleg";
  public static final String UNKNOWN_PARTY_REGISTRATION_ID = "Unknown";

  private AuthenticationConstants() {
    throw new IllegalStateException("Utility class!");
  }
}
