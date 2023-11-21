package se.inera.intyg.minaintyg.integration.intygsadmin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.banner.model.Application;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;

@ExtendWith(MockitoExtension.class)
class BannerFilterServiceTest {

  @InjectMocks
  private BannerFilterService bannerFilterService;

  @Test
  void shouldFilterOnNullValuesForApplication() {
    final var bannerDTOS = new BannerDTO[]{
        BannerDTO.builder()
            .displayFrom(LocalDateTime.now().minusDays(1))
            .displayTo(LocalDateTime.now().plusDays(2))
            .build()
    };
    final var result = bannerFilterService.filter(bannerDTOS);
    assertEquals(0, result.length);
  }

  @Test
  void shouldFilterOnNullValuesForDisplayFromAndTo() {
    final var bannerDTOS = new BannerDTO[]{
        BannerDTO.builder()
            .application(Application.MINA_INTYG)
            .build()
    };
    final var result = bannerFilterService.filter(bannerDTOS);
    assertEquals(0, result.length);
  }

  @Test
  void shouldFilterOnApplicationName() {
    final var bannerDTOS = new BannerDTO[]{
        BannerDTO.builder().build(),
        BannerDTO.builder()
            .application(Application.MINA_INTYG)
            .displayFrom(LocalDateTime.now())
            .displayTo(LocalDateTime.now().plusMinutes(10))
            .build()
    };
    final var result = bannerFilterService.filter(bannerDTOS);
    assertEquals(1, result.length);
  }

  @Test
  void shouldFilterOnDisplayFrom() {
    final var bannerDTOS = new BannerDTO[]{
        BannerDTO.builder()
            .application(Application.MINA_INTYG)
            .displayFrom(LocalDateTime.now().plusDays(1))
            .displayTo(LocalDateTime.now().plusDays(10))
            .build(),
        BannerDTO.builder()
            .application(Application.MINA_INTYG)
            .displayFrom(LocalDateTime.now().minusDays(5))
            .displayTo(LocalDateTime.now().plusDays(10))
            .build()
    };
    final var result = bannerFilterService.filter(bannerDTOS);
    assertEquals(1, result.length);
  }

  @Test
  void shouldFilterOnDisplayTo() {
    final var bannerDTOS = new BannerDTO[]{
        BannerDTO.builder()
            .application(Application.MINA_INTYG)
            .displayFrom(LocalDateTime.now().minusDays(1))
            .displayTo(LocalDateTime.now().minusDays(1))
            .build(),
        BannerDTO.builder()
            .application(Application.MINA_INTYG)
            .displayFrom(LocalDateTime.now().minusDays(5))
            .displayTo(LocalDateTime.now().plusDays(10))
            .build()
    };
    final var result = bannerFilterService.filter(bannerDTOS);
    assertEquals(1, result.length);
  }
}
