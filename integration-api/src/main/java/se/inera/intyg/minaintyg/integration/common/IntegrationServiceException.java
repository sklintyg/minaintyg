package se.inera.intyg.minaintyg.integration.common;


public class IntegrationServiceException extends RuntimeException {

  private final String applicationName;

  public IntegrationServiceException(String message, Throwable cause, String applicationName) {
    super(message, cause);
    this.applicationName = applicationName;
  }
  
  public String getApplicationName() {
    return applicationName;
  }
}
