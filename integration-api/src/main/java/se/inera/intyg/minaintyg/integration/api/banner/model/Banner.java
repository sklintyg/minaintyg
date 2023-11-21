package se.inera.intyg.minaintyg.integration.api.banner.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Banner {

  String id;
  String message;
  LocalDateTime displayFrom;
  LocalDateTime displayTo;
  BannerPriority priority;
}
