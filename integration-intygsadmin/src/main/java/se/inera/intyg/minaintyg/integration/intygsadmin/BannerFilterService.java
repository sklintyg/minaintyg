package se.inera.intyg.minaintyg.integration.intygsadmin;

import java.util.Arrays;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.banner.model.Application;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;
import se.inera.intyg.minaintyg.integration.intygsadmin.util.DateUtil;

@Service
public class BannerFilterService {

  public BannerDTO[] filter(BannerDTO[] bannerDTOS) {
    return Arrays.stream(bannerDTOS)
        .filter(BannerFilterService::isCorrectApplication)
        .filter(BannerFilterService::isActive)
        .toArray(BannerDTO[]::new);
  }

  private static boolean isCorrectApplication(BannerDTO bannerDTO) {
    if (bannerDTO.getApplication() == null) {
      return false;
    }
    return bannerDTO.getApplication().equals(Application.MINA_INTYG);
  }

  private static boolean isActive(BannerDTO bannerDTO) {
    if (bannerDTO.getDisplayFrom() == null || bannerDTO.getDisplayTo() == null) {
      return false;
    }
    return DateUtil.beforeOrEquals(bannerDTO.getDisplayFrom()) && DateUtil.afterOrEquals(
        bannerDTO.getDisplayTo());
  }
}
