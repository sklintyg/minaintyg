package se.inera.intyg.minaintyg.integration.api.certificate.model.common;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AvailableFunction {

  AvailableFunctionType type;
  String name;
  String title;
  String description;
  String body;
  List<Information> information;
  boolean enabled;
}
