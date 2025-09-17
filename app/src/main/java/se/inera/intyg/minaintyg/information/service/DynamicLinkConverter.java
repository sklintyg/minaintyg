package se.inera.intyg.minaintyg.information.service;

import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.information.dto.DynamicLinkDTO;
import se.inera.intyg.minaintyg.information.service.model.DynamicLink;

@Component
public class DynamicLinkConverter {

  public DynamicLinkDTO convert(DynamicLink link) {
    return DynamicLinkDTO.builder()
        .id(link.getId())
        .name(link.getName())
        .url(link.getUrl())
        .build();
  }
}
