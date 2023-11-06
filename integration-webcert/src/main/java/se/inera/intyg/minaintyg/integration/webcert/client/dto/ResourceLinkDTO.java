package se.inera.intyg.minaintyg.integration.webcert.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.ResourceLinkType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceLinkDTO {

  private ResourceLinkType type;
  private String name;
  private String description;
  private boolean enabled;
}