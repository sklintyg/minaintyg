package se.inera.intyg.minaintyg.information.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import se.inera.intyg.minaintyg.config.RedisConfig;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationService;

@ExtendWith(MockitoExtension.class)
class BannerRepositoryTest {

  private static final GetBannerIntegrationResponse EXPECTED_RESPONSE = GetBannerIntegrationResponse.builder()
      .build();
  @Mock
  private CacheManager cacheManager;
  @Mock
  private Cache cache;
  @Mock
  private GetBannerIntegrationService getBannerIntegrationService;

  @InjectMocks
  private BannerRepository bannerRepository;

  @Nested
  class Get {

    @Test
    void shouldGetBannerResponseFromCache() {
      when(cacheManager.getCache(RedisConfig.BANNERS_CACHE)).thenReturn(cache);
      when(cache.get(RedisConfig.BANNERS_CACHE_KEY, GetBannerIntegrationResponse.class)).thenReturn(
          EXPECTED_RESPONSE);

      final var response = bannerRepository.get();

      assertEquals(EXPECTED_RESPONSE, response);
    }

    @Test
    void shouldGetBannerResponseFromBannerIntegrationService() {
      when(cacheManager.getCache(RedisConfig.BANNERS_CACHE)).thenReturn(cache);
      when(cache.get(RedisConfig.BANNERS_CACHE_KEY, GetBannerIntegrationResponse.class)).thenReturn(
          null);
      when(getBannerIntegrationService.get()).thenReturn(EXPECTED_RESPONSE);

      final var response = bannerRepository.get();

      assertEquals(EXPECTED_RESPONSE, response);
    }
  }

  @Nested
  class Load {

    @Test
    void shouldUpdateCache() {
      when(getBannerIntegrationService.get()).thenReturn(EXPECTED_RESPONSE);
      when(cacheManager.getCache(RedisConfig.BANNERS_CACHE)).thenReturn(cache);

      bannerRepository.load();

      verify(cache).put(RedisConfig.BANNERS_CACHE_KEY, EXPECTED_RESPONSE);
    }
  }
}
