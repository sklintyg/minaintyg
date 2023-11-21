package se.inera.intyg.minaintyg.information.service.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Banner {

  String headline;
  String content;
}
