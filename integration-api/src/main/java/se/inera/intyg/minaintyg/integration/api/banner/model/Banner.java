package se.inera.intyg.minaintyg.integration.api.banner.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Banner {

  String id;
  String message;
  BannerPriority priority;
}
