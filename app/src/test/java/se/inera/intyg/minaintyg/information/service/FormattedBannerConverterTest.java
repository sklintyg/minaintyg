package se.inera.intyg.minaintyg.information.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.information.service.dto.FormattedBannerType;
import se.inera.intyg.minaintyg.integration.api.banner.model.Banner;
import se.inera.intyg.minaintyg.integration.api.banner.model.BannerPriority;

class FormattedBannerConverterTest {

  private static final Banner BANNER = Banner.builder()
      .id("ID")
      .message("MESSAGE")
      .priority(BannerPriority.HOG)
      .build();

  private final FormattedBannerConverter formattedBannerConverter = new FormattedBannerConverter();

  @Test
  void shouldSetContent() {
    final var response = formattedBannerConverter.convert(BANNER);

    assertEquals(BANNER.getMessage(), response.getContent());
  }

  @Test
  void shouldSetTypeErrorIfHog() {
    final var response = formattedBannerConverter.convert(
        Banner.builder().priority(BannerPriority.HOG).build());

    assertEquals(FormattedBannerType.ERROR, response.getType());
  }

  @Test
  void shouldSetTypeObserveMedel() {
    final var response = formattedBannerConverter.convert(
        Banner.builder().priority(BannerPriority.MEDEL).build());

    assertEquals(FormattedBannerType.OBSERVE, response.getType());
  }

  @Test
  void shouldSetTypeInfoIfLow() {
    final var response = formattedBannerConverter.convert(
        Banner.builder().priority(BannerPriority.LAG).build());

    assertEquals(FormattedBannerType.INFO, response.getType());
  }

}