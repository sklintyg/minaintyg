package se.inera.intyg.minaintyg.integration.intygsadmin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.banner.model.Application;
import se.inera.intyg.minaintyg.integration.api.banner.model.Banner;
import se.inera.intyg.minaintyg.integration.api.banner.model.BannerPriority;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;

@ExtendWith(MockitoExtension.class)
class BannerConverterTest {

  private static final UUID ID = UUID.randomUUID();
  @InjectMocks
  private BannerConverter bannerConverter;

  private static final Application MINA_INTYG = Application.MINA_INTYG;
  private static final LocalDateTime NOW = LocalDateTime.now();
  private static final String MESSAGE = "message";
  private static final BannerPriority BANNER_PRIORITY = BannerPriority.LAG;
  private static final LocalDateTime DISPLAY_TO = LocalDateTime.now().plusDays(5);
  private static final BannerDTO[] BANNER_DTO = new BannerDTO[]{
      BannerDTO.builder()
          .id(ID)
          .application(MINA_INTYG)
          .createdAt(NOW)
          .displayFrom(NOW)
          .displayTo(DISPLAY_TO)
          .message(MESSAGE)
          .priority(BANNER_PRIORITY)
          .build()
  };

  private static final List<Banner> EXPECTED_BANNER = List.of(
      Banner.builder()
          .id(ID)
          .application(Application.MINA_INTYG)
          .createdAt(NOW)
          .displayFrom(NOW)
          .displayTo(DISPLAY_TO)
          .message(MESSAGE)
          .priority(BANNER_PRIORITY)
          .build()
  );

  @Test
  void shouldReturnEmptyListIfNoActiveBannersArePresent() {
    final var result = bannerConverter.convert(new BannerDTO[]{});
    assertTrue(result.isEmpty());
  }

  @Test
  void shouldConvertId() {
    final var result = bannerConverter.convert(BANNER_DTO);
    assertEquals(EXPECTED_BANNER.get(0).getId(), result.get(0).getId());
  }

  @Test
  void shouldConvertApplication() {
    final var result = bannerConverter.convert(BANNER_DTO);
    assertEquals(EXPECTED_BANNER.get(0).getApplication(), result.get(0).getApplication());
  }

  @Test
  void shouldConvertCreatedAt() {
    final var result = bannerConverter.convert(BANNER_DTO);
    assertEquals(EXPECTED_BANNER.get(0).getCreatedAt(), result.get(0).getCreatedAt());
  }

  @Test
  void shouldConverDisplayFrom() {
    final var result = bannerConverter.convert(BANNER_DTO);
    assertEquals(EXPECTED_BANNER.get(0).getDisplayFrom(), result.get(0).getDisplayFrom());
  }

  @Test
  void shouldConvertDisplayTo() {
    final var result = bannerConverter.convert(BANNER_DTO);
    assertEquals(EXPECTED_BANNER.get(0).getDisplayTo(), result.get(0).getDisplayTo());
  }

  @Test
  void shouldConvertMessage() {
    final var result = bannerConverter.convert(BANNER_DTO);
    assertEquals(EXPECTED_BANNER.get(0).getMessage(), result.get(0).getMessage());
  }

  @Test
  void shouldConvertPriority() {
    final var result = bannerConverter.convert(BANNER_DTO);
    assertEquals(EXPECTED_BANNER.get(0).getPriority(), result.get(0).getPriority());
  }
}
