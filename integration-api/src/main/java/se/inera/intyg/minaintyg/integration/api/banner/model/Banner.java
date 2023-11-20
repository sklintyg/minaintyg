package se.inera.intyg.minaintyg.integration.api.banner.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Banner {

  UUID id;
  LocalDateTime createdAt;
  Application application;
  String message;
  LocalDateTime displayFrom;
  LocalDateTime displayTo;
  BannerPriority priority;
}
