package se.inera.intyg.minaintyg.information.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private BannerRepository bannerRepository;

  @Nested
  class Get {

    @Test
    void shouldGetBannerResponseFromCache() throws JsonProcessingException {
      when(cacheManager.getCache(RedisConfig.BANNERS_CACHE)).thenReturn(cache);
      when(cache.get(RedisConfig.BANNERS_CACHE_KEY, String.class)).thenReturn(
          EXPECTED_RESPONSE.toString());
      when(objectMapper.readValue(EXPECTED_RESPONSE.toString(),
          GetBannerIntegrationResponse.class)).thenReturn(EXPECTED_RESPONSE);

      final var response = bannerRepository.get();

      assertEquals(EXPECTED_RESPONSE, response);
    }

    @Test
    void shouldGetBannerResponseFromBannerIntegrationService() {
      when(cacheManager.getCache(RedisConfig.BANNERS_CACHE)).thenReturn(cache);
      when(cache.get(RedisConfig.BANNERS_CACHE_KEY, String.class)).thenReturn(
          null);
      when(getBannerIntegrationService.get()).thenReturn(EXPECTED_RESPONSE);

      final var response = bannerRepository.get();

      assertEquals(EXPECTED_RESPONSE, response);
    }
  }

  @Nested
  class Load {

    @Test
    void shouldUpdateCache() throws JsonProcessingException {
      when(getBannerIntegrationService.get()).thenReturn(EXPECTED_RESPONSE);
      when(cacheManager.getCache(RedisConfig.BANNERS_CACHE)).thenReturn(cache);
      when(objectMapper.writeValueAsString(EXPECTED_RESPONSE)).thenReturn(
          EXPECTED_RESPONSE.toString());

      bannerRepository.load();

      verify(cache).put(RedisConfig.BANNERS_CACHE_KEY, EXPECTED_RESPONSE.toString());
    }

    @Test
    void shouldReturnBannerResponse() {
      when(getBannerIntegrationService.get()).thenReturn(EXPECTED_RESPONSE);
      when(cacheManager.getCache(RedisConfig.BANNERS_CACHE)).thenReturn(cache);

      final var result = bannerRepository.load();

      assertEquals(EXPECTED_RESPONSE, result);
    }
  }
}
