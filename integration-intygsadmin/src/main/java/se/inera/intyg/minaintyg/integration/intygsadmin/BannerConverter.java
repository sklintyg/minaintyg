package se.inera.intyg.minaintyg.integration.intygsadmin;

import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.banner.model.Banner;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;

@Component
public class BannerConverter {

  public List<Banner> convert(List<BannerDTO> banners) {
    return banners.stream()
        .map(BannerConverter::toBanner)
        .toList();
  }

  private static Banner toBanner(BannerDTO bannerDTO) {
    return Banner.builder()
        .id(bannerDTO.getId().toString())
        .message(bannerDTO.getMessage())
        .priority(bannerDTO.getPriority())
        .build();
  }
}
