package se.inera.intyg.minaintyg.information.service;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.config.RedisConfig;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationService;

@Service
@RequiredArgsConstructor
public class BannerRepository {

  private final CacheManager cacheManager;
  private final GetBannerIntegrationService getBannerIntegrationService;

  public GetBannerIntegrationResponse get() {
    return getBannerResponseFromCache().orElseGet(this::load);
  }

  public GetBannerIntegrationResponse load() {
    final var getBannerIntegrationResponse = getBannerIntegrationService.get();
    Objects.requireNonNull(cacheManager.getCache(RedisConfig.BANNERS_CACHE))
        .put(RedisConfig.BANNERS_CACHE_KEY, getBannerIntegrationResponse);
    return getBannerIntegrationResponse;
  }

  private Optional<GetBannerIntegrationResponse> getBannerResponseFromCache() {
    return Optional.ofNullable(
        Objects.requireNonNull(cacheManager.getCache(RedisConfig.BANNERS_CACHE))
            .get(RedisConfig.BANNERS_CACHE_KEY, GetBannerIntegrationResponse.class)
    );
  }
}
