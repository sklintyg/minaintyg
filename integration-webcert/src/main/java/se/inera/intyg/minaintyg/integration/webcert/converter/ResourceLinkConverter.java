package se.inera.intyg.minaintyg.integration.webcert.converter;

import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.ResourceLink;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.ResourceLinkDTO;

@Component
public class ResourceLinkConverter {

  public ResourceLink convert(ResourceLinkDTO link) {
    return ResourceLink
        .builder()
        .name(link.getName())
        .type(link.getType())
        .description(link.getDescription())
        .enabled(link.isEnabled())
        .build();
  }
}
