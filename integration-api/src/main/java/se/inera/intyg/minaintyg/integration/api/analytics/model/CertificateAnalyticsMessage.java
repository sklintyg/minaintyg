package se.inera.intyg.minaintyg.integration.api.analytics.model;

import java.io.Serializable;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificateAnalyticsMessage implements Serializable {

  /**
   * Unique identifier for the message.
   */
  String messageId = UUID.randomUUID().toString();
  /**
   * Type of the message, used for routing and processing.
   */
  String type = "certificate.analytics.event";
  /**
   * Version of the message schema.
   */
  String schemaVersion = "v1";

  AnalyticsCertificate certificate;
  AnalyticsEvent event;

}
