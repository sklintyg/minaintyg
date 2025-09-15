package se.inera.intyg.minaintyg.information.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.information.dto.DynamicLinkDTO;

@Service
@RequiredArgsConstructor
public class GetDynamicLinksService {

  private final DynamicLinkRepository dynamicLinkRepository;
  private final DynamicLinkConverter dynamicLinkConverter;
  @Value("${application.environment}")
  private String environmentType;
  @Value("${1177.menu.setting.link}")
  private String menuSettingLink;

  public List<DynamicLinkDTO> get() {
    return dynamicLinkRepository.get(environmentType, menuSettingLink).stream()
        .map(dynamicLinkConverter::convert)
        .toList();
  }
}
