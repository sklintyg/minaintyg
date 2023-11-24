package se.inera.intyg.minaintyg.information.service;

import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.information.service.dto.FormattedBanner;
import se.inera.intyg.minaintyg.information.service.dto.FormattedBannerType;
import se.inera.intyg.minaintyg.integration.api.banner.model.Banner;
import se.inera.intyg.minaintyg.integration.api.banner.model.BannerPriority;

@Component
public class FormattedBannerConverter {

  public FormattedBanner convert(Banner banner) {
    return FormattedBanner.builder()
        .content(banner.getMessage())
        .type(convertType(banner.getPriority()))
        .build();
  }

  private FormattedBannerType convertType(BannerPriority priority) {
    return switch (priority) {
      case HIGH -> FormattedBannerType.ERROR;
      case MEDIUM -> FormattedBannerType.OBSERVE;
      case LOW -> FormattedBannerType.INFO;
    };
  }
}
