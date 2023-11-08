package se.inera.intyg.minaintyg.integration.api.certificate.model.common;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Information {

  InformationType type;
  String id;
}
