package se.inera.intyg.minaintyg.integration.intygsadmin;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.banner.model.Banner;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;

@Component
public class BannerConverter {

  public List<Banner> convert(BannerDTO[] banners) {
    return Arrays.stream(banners)
        .map(BannerConverter::toBanner)
        .toList();
  }

  private static Banner toBanner(BannerDTO bannerDTO) {
    return Banner.builder()
        .id(bannerDTO.getId())
        .application(bannerDTO.getApplication())
        .createdAt(bannerDTO.getCreatedAt())
        .displayFrom(bannerDTO.getDisplayFrom())
        .displayTo(bannerDTO.getDisplayTo())
        .message(bannerDTO.getMessage())
        .priority(bannerDTO.getPriority())
        .build();
  }
}
