package se.inera.intyg.minaintyg.integration.api.certificate.model;

import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.ResourceLinkType;

@Value
@Builder
public class ResourceLink {

  ResourceLinkType type;
  String name;
  String description;
  boolean enabled;
}
