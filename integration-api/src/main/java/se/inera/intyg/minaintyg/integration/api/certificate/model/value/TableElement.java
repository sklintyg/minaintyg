package se.inera.intyg.minaintyg.integration.api.certificate.model.value;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TableElement {

  String value;
  TableElementType type;
}
