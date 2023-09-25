package se.inera.intyg.minaintyg.integration.api.certificate.model.list;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificateSummary {

  String label;
  String value;
}
