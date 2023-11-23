package se.inera.intyg.minaintyg.jobs;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import se.inera.intyg.minaintyg.config.RedisConfig;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationService;

@ExtendWith(MockitoExtension.class)
class BannerJobTest {

  @Mock
  private GetBannerIntegrationService getBannerIntegrationService;
  @Mock
  private RedisCacheManager cacheManager;

  @Mock
  private Cache cache;
  @InjectMocks
  private BannerJob bannerJob;

  private static final GetBannerIntegrationResponse GET_BANNER_INTEGRATION_RESPONSE = GetBannerIntegrationResponse.builder()
      .build();

  @Test
  void shouldUpdateCache() {
    when(getBannerIntegrationService.get()).thenReturn(GET_BANNER_INTEGRATION_RESPONSE);
    when(cacheManager.getCache(RedisConfig.BANNERS_CACHE)).thenReturn(cache);

    bannerJob.executeBannerJob();

    verify(cache).put(RedisConfig.BANNERS_CACHE_KEY, GET_BANNER_INTEGRATION_RESPONSE);
  }
}
