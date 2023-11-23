package se.inera.intyg.minaintyg.integration.intygsadmin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.ApplicationDTO;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;

@ExtendWith(MockitoExtension.class)
class BannerFilterServiceTest {

  @InjectMocks
  private BannerFilterService bannerFilterService;

  @Test
  void shouldFilterOnNullValuesForApplication() {
    final var bannerDTOS = List.of(
        BannerDTO.builder()
            .displayFrom(LocalDateTime.now().minusDays(1))
            .displayTo(LocalDateTime.now().plusDays(2))
            .build()
    );

    final var result = bannerFilterService.filter(bannerDTOS);

    assertTrue(result.isEmpty());
  }

  @Test
  void shouldFilterOnNullValuesForDisplayFromAndTo() {
    final var bannerDTOS = List.of(
        BannerDTO.builder()
            .application(ApplicationDTO.MINA_INTYG)
            .build()
    );

    final var result = bannerFilterService.filter(bannerDTOS);

    assertTrue(result.isEmpty());
  }

  @Test
  void shouldFilterOnApplicationName() {
    final var expectedBanner = BannerDTO.builder()
        .application(ApplicationDTO.MINA_INTYG)
        .displayFrom(LocalDateTime.now())
        .displayTo(LocalDateTime.now().plusMinutes(10))
        .build();

    final var bannerDTOS = List.of(
        BannerDTO.builder().build(),
        BannerDTO.builder()
            .application(ApplicationDTO.MINA_INTYG)
            .displayFrom(LocalDateTime.now())
            .displayTo(LocalDateTime.now().plusMinutes(10))
            .build()
    );

    final var result = bannerFilterService.filter(bannerDTOS);

    assertEquals(expectedBanner, result.get(0));
  }

  @Test
  void shouldFilterOnDisplayFrom() {
    final var expectedBanner = BannerDTO.builder()
        .application(ApplicationDTO.MINA_INTYG)
        .displayFrom(LocalDateTime.now().minusDays(5))
        .displayTo(LocalDateTime.now().plusDays(10))
        .build();

    final var bannerDTOS = List.of(
        BannerDTO.builder()
            .application(ApplicationDTO.MINA_INTYG)
            .displayFrom(LocalDateTime.now().plusDays(1))
            .displayTo(LocalDateTime.now().plusDays(10))
            .build(),
        expectedBanner
    );

    final var result = bannerFilterService.filter(bannerDTOS);

    assertEquals(expectedBanner, result.get(0));
  }

  @Test
  void shouldFilterOnDisplayTo() {
    final var expectedBanner = BannerDTO.builder()
        .application(ApplicationDTO.MINA_INTYG)
        .displayFrom(LocalDateTime.now().minusDays(5))
        .displayTo(LocalDateTime.now().plusDays(10))
        .build();

    final var bannerDTOS = List.of(
        BannerDTO.builder()
            .application(ApplicationDTO.MINA_INTYG)
            .displayFrom(LocalDateTime.now().minusDays(1))
            .displayTo(LocalDateTime.now().minusDays(1))
            .build(),
        expectedBanner
    );

    final var result = bannerFilterService.filter(bannerDTOS);

    assertEquals(expectedBanner, result.get(0));
  }
}
