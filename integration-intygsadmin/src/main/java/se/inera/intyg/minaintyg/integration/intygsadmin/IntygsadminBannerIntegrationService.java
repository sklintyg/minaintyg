package se.inera.intyg.minaintyg.integration.intygsadmin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationService;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.GetBannersFromIntygsadminService;

@Service
@RequiredArgsConstructor
public class IntygsadminBannerIntegrationService implements GetBannerIntegrationService {

  private final GetBannersFromIntygsadminService getBannersFromIntygsadminService;
  private final BannerConverter bannerConverter;
  private final BannerFilterService bannersFilterService;

  @Override
  public GetBannerIntegrationResponse get() {
    final var banners = getBannersFromIntygsadminService.get();
    final var filteredBanners = bannersFilterService.filter(banners);
    return GetBannerIntegrationResponse.builder()
        .banners(bannerConverter.convert(filteredBanners))
        .build();
  }
}
