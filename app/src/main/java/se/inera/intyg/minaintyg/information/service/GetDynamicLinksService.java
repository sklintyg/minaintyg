package se.inera.intyg.minaintyg.information.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.information.dto.DynamicLinkDTO;

@Service
@RequiredArgsConstructor
public class GetDynamicLinksService {

  private final DynamicLinkRepository dynamicLinkRepository;
  private final DynamicLinkConverter dynamicLinkConverter;

  public List<DynamicLinkDTO> get() {
    return dynamicLinkRepository.get().stream()
        .map(dynamicLinkConverter::convert)
        .toList();
  }
}
