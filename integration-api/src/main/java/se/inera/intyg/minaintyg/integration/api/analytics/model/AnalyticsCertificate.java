package se.inera.intyg.minaintyg.integration.api.analytics.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AnalyticsCertificate {

  String id;
  String type;
  String typeVersion;
  String patientId;
  String unitId;
  String careProviderId;
}
