package se.inera.intyg.minaintyg.integration.intygsadmin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationResponse;
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
  private GetBannersFromIntygsadminService bannersFromIntygsadminService;
  @Mock
  private BannerFilterService bannerFilterService;

  @InjectMocks
  private IntygsadminBannerIntegrationService intygsadminBannerIntegrationService;


  @Test
  void shouldReturnListOfBannersIntygsadmin() {
    final var expectedResponse = GetBannerIntegrationResponse.builder()
        .banners(CONVERTED_BANNERS)
        .build();

    when(bannersFromIntygsadminService.get()).thenReturn(
        INTYGSADMIN_RESPONSE
    );
    when(bannerFilterService.filter(INTYGSADMIN_RESPONSE)).thenReturn(INTYGSADMIN_RESPONSE);
    when(bannerConverter.convert(INTYGSADMIN_RESPONSE)).thenReturn(CONVERTED_BANNERS);

    final var result = intygsadminBannerIntegrationService.get();

    assertEquals(expectedResponse, result);
  }
}
