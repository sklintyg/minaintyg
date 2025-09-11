package se.inera.intyg.minaintyg.information.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class FormattedDynamicLink {

  String id;
  String name;
  String url;
}
