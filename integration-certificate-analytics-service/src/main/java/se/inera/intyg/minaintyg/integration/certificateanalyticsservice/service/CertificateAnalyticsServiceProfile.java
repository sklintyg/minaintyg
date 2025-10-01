package se.inera.intyg.minaintyg.integration.certificateanalyticsservice.service;

public interface CertificateAnalyticsServiceProfile {

  default boolean isEnabled() {
    return false;
  }
}
