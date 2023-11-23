package se.inera.intyg.minaintyg.integration.intygsadmin;

import java.util.List;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.ApplicationDTO;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;
import se.inera.intyg.minaintyg.integration.intygsadmin.util.DateUtil;

@Service
public class BannerFilterService {

  public List<BannerDTO> filter(List<BannerDTO> bannerDTOS) {
    return bannerDTOS.stream()
        .filter(BannerFilterService::isCorrectApplication)
        .filter(BannerFilterService::isActive)
        .toList();
  }

  private static boolean isCorrectApplication(BannerDTO bannerDTO) {
    if (bannerDTO.getApplication() == null) {
      return false;
    }
    return bannerDTO.getApplication().equals(ApplicationDTO.MINA_INTYG);
  }

  private static boolean isActive(BannerDTO bannerDTO) {
    if (bannerDTO.getDisplayFrom() == null || bannerDTO.getDisplayTo() == null) {
      return false;
    }
    return DateUtil.beforeOrEquals(bannerDTO.getDisplayFrom()) && DateUtil.afterOrEquals(
        bannerDTO.getDisplayTo());
  }
}
