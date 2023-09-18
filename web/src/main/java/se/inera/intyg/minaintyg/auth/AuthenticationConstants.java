package se.inera.intyg.minaintyg.auth;

public final class AuthenticationConstants {

  public static final String PERSON_ID_ATTRIBUTE = "Subject_SerialNumber";
  public static final String ELEG_PARTY_REGISTRATION_ID = "eleg";
  public static final String SINGLE_LOGOUT_SERVICE_LOCATION = "{baseUrl}/logout/saml2/slo";
  public static final String UNKNOWN_PARTY_REGISTRATION_ID = "Unknown";

  private AuthenticationConstants() {
    throw new IllegalStateException("Utility class!");
  }
}
