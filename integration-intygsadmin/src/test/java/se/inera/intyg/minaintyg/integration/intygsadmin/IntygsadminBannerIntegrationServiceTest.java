package se.inera.intyg.minaintyg.integration.intygsadmin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.banner.model.Application;
import se.inera.intyg.minaintyg.integration.api.banner.model.Banner;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.GetBannersFromIntygsadminService;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;

@ExtendWith(MockitoExtension.class)
class IntygsadminBannerIntegrationServiceTest {

  private static final BannerDTO[] INTYGSADMIN_RESPONSE = {BannerDTO.builder().build()};
  private static final List<Banner> CONVERTED_BANNERS = List.of(Banner.builder().build());
  @Mock
  private BannerConverter bannerConverter;

  @Mock
  private Cache cache;
  @Mock
  private GetBannersFromIntygsadminService bannersFromIntygsadminService;
  @Mock
  private RedisCacheManager cacheManager;
  @Mock
  private BannerFilterService bannerFilterService;

  @InjectMocks
  private IntygsadminBannerIntegrationService intygsadminBannerIntegrationService;

  @Test
  void shouldReturnListOfBannersFromCache() {
    final var expectedResponse = GetBannerIntegrationResponse.builder()
        .banners(CONVERTED_BANNERS)
        .build();
    when(cacheManager.getCache(anyString())).thenReturn(cache);
    when(cache.get(Application.MINA_INTYG, BannerDTO[].class)).thenReturn(INTYGSADMIN_RESPONSE);
    when(bannerConverter.convert(INTYGSADMIN_RESPONSE)).thenReturn(CONVERTED_BANNERS);
    final var result = intygsadminBannerIntegrationService.get();
    assertEquals(expectedResponse, result);
  }

  @Test
  void shouldReturnListOfBannersIntygsadmin() {
    final var expectedResponse = GetBannerIntegrationResponse.builder()
        .banners(CONVERTED_BANNERS)
        .build();
    when(cacheManager.getCache(anyString())).thenReturn(cache);
    when(cache.get(Application.MINA_INTYG, BannerDTO[].class)).thenReturn(null);
    when(bannersFromIntygsadminService.get()).thenReturn(
        INTYGSADMIN_RESPONSE
    );
    when(bannerFilterService.filter(INTYGSADMIN_RESPONSE)).thenReturn(INTYGSADMIN_RESPONSE);
    when(bannerConverter.convert(INTYGSADMIN_RESPONSE)).thenReturn(CONVERTED_BANNERS);
    final var result = intygsadminBannerIntegrationService.get();
    assertEquals(expectedResponse, result);
  }
}
