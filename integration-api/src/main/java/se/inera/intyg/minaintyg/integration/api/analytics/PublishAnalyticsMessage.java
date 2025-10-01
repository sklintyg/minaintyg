package se.inera.intyg.minaintyg.integration.api.analytics;

import se.inera.intyg.minaintyg.integration.api.analytics.model.CertificateAnalyticsMessage;

public interface PublishAnalyticsMessage {

  void publishEvent(CertificateAnalyticsMessage message);
}
