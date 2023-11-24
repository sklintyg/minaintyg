package se.inera.intyg.minaintyg.integration.intygsadmin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.banner.model.Banner;
import se.inera.intyg.minaintyg.integration.api.banner.model.BannerPriority;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.ApplicationDTO;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerPriorityDTO;

@ExtendWith(MockitoExtension.class)
class BannerConverterTest {

  private static final UUID ID = UUID.randomUUID();
  @InjectMocks
  private BannerConverter bannerConverter;

  private static final ApplicationDTO MINA_INTYG = ApplicationDTO.MINA_INTYG;
  private static final LocalDateTime NOW = LocalDateTime.now();
  private static final String MESSAGE = "message";
  private static final BannerPriorityDTO BANNER_DTO_PRIORITY_LAG = BannerPriorityDTO.LAG;
  private static final BannerPriorityDTO BANNER_DTO_PRIORITY_MEDEL = BannerPriorityDTO.MEDEL;
  private static final BannerPriorityDTO BANNER_DTO_PRIORITY_HÖG = BannerPriorityDTO.HOG;
  private static final BannerPriority BANNER_PRIORITY_LOW = BannerPriority.LOW;
  private static final BannerPriority BANNER_PRIORITY_MEDIUM = BannerPriority.MEDIUM;
  private static final BannerPriority BANNER_PRIORITY_HIGH = BannerPriority.HIGH;
  private static final LocalDateTime DISPLAY_TO = LocalDateTime.now().plusDays(5);

  private List<BannerDTO> getBannerDTOS(BannerPriorityDTO priority) {
    return List.of(
        BannerDTO.builder()
            .id(ID)
            .application(MINA_INTYG)
            .createdAt(NOW)
            .displayFrom(NOW)
            .displayTo(DISPLAY_TO)
            .message(MESSAGE)
            .priority(priority)
            .build()
    );
  }

  private List<Banner> getBanners(BannerPriority priority) {
    return List.of(
        Banner.builder()
            .id(ID.toString())
            .message(MESSAGE)
            .priority(priority)
            .build()
    );
  }

  @Test
  void shouldReturnEmptyListIfNoActiveBannersArePresent() {
    final var result = bannerConverter.convert(Collections.emptyList());
    assertTrue(result.isEmpty());
  }

  @Test
  void shouldConvertId() {
    final var banners = getBanners(BannerPriority.LOW);
    final var result = bannerConverter.convert(getBannerDTOS(BannerPriorityDTO.LAG));
    assertEquals(banners.get(0).getId(), result.get(0).getId());
  }

  @Test
  void shouldConvertMessage() {
    final var banners = getBanners(BannerPriority.LOW);
    final var result = bannerConverter.convert(getBannerDTOS(BannerPriorityDTO.LAG));
    assertEquals(banners.get(0).getMessage(), result.get(0).getMessage());
  }

  @Test
  void shouldConvertPriorityLow() {
    final var banners = getBanners(BannerPriority.LOW);
    final var result = bannerConverter.convert(getBannerDTOS(BannerPriorityDTO.LAG));
    assertEquals(banners.get(0).getPriority(), result.get(0).getPriority());
  }

  @Test
  void shouldConvertPriorityMedium() {
    final var banners = getBanners(BannerPriority.MEDIUM);
    final var result = bannerConverter.convert(getBannerDTOS(BannerPriorityDTO.MEDEL));
    assertEquals(banners.get(0).getPriority(), result.get(0).getPriority());
  }

  @Test
  void shouldConvertPriorityHigh() {
    final var banners = getBanners(BannerPriority.HIGH);
    final var result = bannerConverter.convert(getBannerDTOS(BannerPriorityDTO.HOG));
    assertEquals(banners.get(0).getPriority(), result.get(0).getPriority());
  }
}
