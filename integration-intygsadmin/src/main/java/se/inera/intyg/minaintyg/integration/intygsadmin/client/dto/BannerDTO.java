package se.inera.intyg.minaintyg.integration.intygsadmin.client.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.inera.intyg.minaintyg.integration.api.banner.model.Application;
import se.inera.intyg.minaintyg.integration.api.banner.model.BannerPriority;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private UUID id;
  private LocalDateTime createdAt;
  private Application application;
  private String message;
  private LocalDateTime displayFrom;
  private LocalDateTime displayTo;
  private BannerPriority priority;
}
