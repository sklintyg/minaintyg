package se.inera.intyg.minaintyg.integration.api.analytics.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AnalyticsEvent {

  LocalDateTime timestamp;
  CertificateAnalyticsMessageType messageType;
  String userId;
  String role;
  String unitId;
  String careProviderId;
  String origin;
  String sessionId;
}
