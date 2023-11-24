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
      .priority(BannerPriority.HIGH)
      .build();

  private final FormattedBannerConverter formattedBannerConverter = new FormattedBannerConverter();

  @Test
  void shouldSetContent() {
    final var response = formattedBannerConverter.convert(BANNER);

    assertEquals(BANNER.getMessage(), response.getContent());
  }

  @Test
  void shouldSetTypeErrorIfHigh() {
    final var response = formattedBannerConverter.convert(
        Banner.builder().priority(BannerPriority.HIGH).build());

    assertEquals(FormattedBannerType.ERROR, response.getType());
  }

  @Test
  void shouldSetTypeObserveMedium() {
    final var response = formattedBannerConverter.convert(
        Banner.builder().priority(BannerPriority.MEDIUM).build());

    assertEquals(FormattedBannerType.OBSERVE, response.getType());
  }

  @Test
  void shouldSetTypeInfoIfLow() {
    final var response = formattedBannerConverter.convert(
        Banner.builder().priority(BannerPriority.LOW).build());

    assertEquals(FormattedBannerType.INFO, response.getType());
  }

}