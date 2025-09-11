package se.inera.intyg.minaintyg.information.service;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.information.dto.DynamicLink;

@Service
@RequiredArgsConstructor
public class GetEnvironmentConfigService {

  @Getter
  @Value("${application.environment}")
  private String environmentType;
  private final EnvironmentConfigRepository environmentConfigRepository;

  public Map<String, DynamicLink> get() {
    return environmentConfigRepository.get();
  }
}
