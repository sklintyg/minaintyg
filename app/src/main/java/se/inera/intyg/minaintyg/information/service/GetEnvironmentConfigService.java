package se.inera.intyg.minaintyg.information.service;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.information.dto.DynamicLink;
import se.inera.intyg.minaintyg.information.dto.FormattedDynamicLink;

@Service
@RequiredArgsConstructor
public class GetEnvironmentConfigService {

  @Getter
  @Value("${application.environment}")
  private String environmentType;
  private final EnvironmentConfigRepository environmentConfigRepository;

  public List<FormattedDynamicLink> get() {
    return environmentConfigRepository.get().stream()
        .map(this::getFormattedDynamicLink)
        .toList();
  }

  private FormattedDynamicLink getFormattedDynamicLink(DynamicLink link) {
    return FormattedDynamicLink.builder()
        .id(link.getId())
        .name(link.getName())
        .url(link.getUrl().get(environmentType))
        .build();
  }
}
