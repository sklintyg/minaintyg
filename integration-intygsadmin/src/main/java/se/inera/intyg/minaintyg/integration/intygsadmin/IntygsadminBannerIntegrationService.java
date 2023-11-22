package se.inera.intyg.minaintyg.integration.intygsadmin;

import static se.inera.intyg.minaintyg.integration.intygsadmin.configuration.RedisConfig.BANNERS;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationService;
import se.inera.intyg.minaintyg.integration.api.banner.model.Application;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.GetBannersFromIntygsadminService;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;

@Service
@Slf4j
@RequiredArgsConstructor
public class IntygsadminBannerIntegrationService implements GetBannerIntegrationService {

  private final GetBannersFromIntygsadminService getBannersFromIntygsadminService;
  private final BannerConverter bannerConverter;
  private final BannerFilterService bannersFilterService;
  private final RedisCacheManager cacheManager;

  @Override
  public GetBannerIntegrationResponse get() {
    return GetBannerIntegrationResponse.builder()
        .banners(
            bannerConverter.convert(
                getBannersFromCache().orElseGet(
                    () -> getBannersFromIntygsadmin(Application.MINA_INTYG)
                )
            )
        )
        .build();
  }

  public BannerDTO[] getBannersFromIntygsadmin(Application application) {
    final var banners = getBannersFromIntygsadminService.get();
    final var filteredBanners = bannersFilterService.filter(banners);
    Objects.requireNonNull(cacheManager.getCache(BANNERS))
        .put(application, filteredBanners);
    return filteredBanners;
  }

  private Optional<BannerDTO[]> getBannersFromCache() {
    return Optional.ofNullable(
        Objects.requireNonNull(cacheManager.getCache(BANNERS))
            .get(Application.MINA_INTYG, BannerDTO[].class)
    );
  }
}
