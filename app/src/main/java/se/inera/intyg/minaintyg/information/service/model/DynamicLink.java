package se.inera.intyg.minaintyg.information.service.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DynamicLink {

  String id;
  String name;
  String url;
}
