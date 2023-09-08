package se.inera.intyg.minaintyg.auth;

public enum LoginMethod {
  ELVA77, FAKE;

  public String value() {
    return name();
  }

  public static LoginMethod fromValue(String v) {
    return valueOf(v);
  }
}
