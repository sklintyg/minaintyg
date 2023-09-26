package se.inera.intyg.minaintyg.auth;

public enum LoginMethod {
  ELVA77, FAKE;

  public String value() {
    return name();
  }
  
}
