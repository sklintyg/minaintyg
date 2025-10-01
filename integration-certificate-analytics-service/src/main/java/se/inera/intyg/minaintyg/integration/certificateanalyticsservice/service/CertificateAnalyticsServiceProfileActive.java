package se.inera.intyg.minaintyg.integration.certificateanalyticsservice.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("certificate-analytics-service-active")
public class CertificateAnalyticsServiceProfileActive implements
    CertificateAnalyticsServiceProfile {

  @Override
  public boolean isEnabled() {
    return true;
  }
}
