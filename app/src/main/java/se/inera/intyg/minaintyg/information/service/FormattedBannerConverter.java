package se.inera.intyg.minaintyg.information.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.information.service.dto.FormattedBanner;
import se.inera.intyg.minaintyg.information.service.dto.FormattedBannerType;
import se.inera.intyg.minaintyg.integration.api.banner.model.Banner;
import se.inera.intyg.minaintyg.integration.api.banner.model.BannerPriority;

@Service
@RequiredArgsConstructor
public class FormattedBannerConverter {

  public FormattedBanner convert(Banner banner) {
    return FormattedBanner.builder()
        .content(banner.getMessage())
        .type(convertType(banner.getPriority()))
        .build();
  }

  private FormattedBannerType convertType(BannerPriority priority) {
    return switch (priority) {
      case HOG -> FormattedBannerType.ERROR;
      case MEDEL -> FormattedBannerType.OBSERVE;
      case LAG -> FormattedBannerType.INFO;
    };
  }
}
