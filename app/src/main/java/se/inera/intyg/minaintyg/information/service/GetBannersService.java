package se.inera.intyg.minaintyg.information.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.information.service.dto.FormattedBanner;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationService;

@Service
@RequiredArgsConstructor
public class GetBannersService {

  private final GetBannerIntegrationService getBannerIntegrationService;
  private final FormattedBannerConverter formattedBannerConverter;

  public List<FormattedBanner> get() {
    final var response = getBannerIntegrationService.get();

    return response.getBanners()
        .stream()
        .map(formattedBannerConverter::convert)
        .toList();
  }
}
