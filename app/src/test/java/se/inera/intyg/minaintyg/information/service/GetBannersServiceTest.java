package se.inera.intyg.minaintyg.information.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.information.service.dto.FormattedBanner;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.banner.model.Banner;

@ExtendWith(MockitoExtension.class)
class GetBannersServiceTest {

  private static final Banner EXPECTED_BANNER = Banner.builder().build();
  private static final List<Banner> EXPECTED_BANNERS = List.of(
      EXPECTED_BANNER,
      EXPECTED_BANNER
  );
  private static final FormattedBanner EXPECTED_FORMATTED_BANNER = FormattedBanner.builder()
      .build();
  @Mock
  private BannerRepository bannerRepository;

  @Mock
  private FormattedBannerConverter formattedBannerConverter;

  @InjectMocks
  private GetBannersService getBannersService;

  @BeforeEach
  void setup() {
    when(formattedBannerConverter.convert(any(Banner.class)))
        .thenReturn(EXPECTED_FORMATTED_BANNER);

    when(bannerRepository.get())
        .thenReturn(
            GetBannerIntegrationResponse.builder()
                .banners(EXPECTED_BANNERS)
                .build()
        );
  }

  @Test
  void shouldConvertBannersFromIntegrationResponse() {
    final var captor = ArgumentCaptor.forClass(Banner.class);

    getBannersService.get();

    verify(formattedBannerConverter, times(2)).convert(captor.capture());
    assertEquals(EXPECTED_BANNERS.get(0), captor.getValue());
  }

  @Test
  void shouldReturnFormattedBanners() {
    final var response = getBannersService.get();

    assertEquals(List.of(EXPECTED_FORMATTED_BANNER, EXPECTED_FORMATTED_BANNER), response);
  }
}
