package se.inera.intyg.minaintyg.information.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.information.dto.DynamicLinkDTO;
import se.inera.intyg.minaintyg.information.service.model.Elva77MenuConfig;

@Service
@RequiredArgsConstructor
public class GetDynamicLinksService {

  private final DynamicLinkRepository dynamicLinkRepository;
  private final DynamicLinkConverter dynamicLinkConverter;
  @Value("${application.environment}")
  private String environmentType;


  public Elva77MenuConfig getMenuConfig() {
    return dynamicLinkRepository.get();
  }

  public List<DynamicLinkDTO> get() {
    return dynamicLinkRepository.get(environmentType).stream()
        .map(link -> dynamicLinkConverter.convert(link, environmentType))
        .toList();
  }
}
