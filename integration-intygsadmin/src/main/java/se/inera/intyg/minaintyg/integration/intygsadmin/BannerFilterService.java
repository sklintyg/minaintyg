package se.inera.intyg.minaintyg.integration.intygsadmin;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.banner.model.Application;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;

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
    if (isNull(bannerDTO.getDisplayFrom()) || isNull(bannerDTO.getDisplayTo())) {
      return false;
    }
    return beforeOrEquals(bannerDTO.getDisplayFrom()) && afterOrEquals(bannerDTO.getDisplayTo());
  }

  private static boolean afterOrEquals(LocalDateTime dateTime) {
    return dateTime.isAfter(LocalDateTime.now()) || dateTime.isEqual(LocalDateTime.now());
  }

  private static boolean beforeOrEquals(LocalDateTime dateTime) {
    return dateTime.isBefore(LocalDateTime.now()) || dateTime.isEqual(LocalDateTime.now());
  }

  private static boolean isNull(LocalDateTime dateTime) {
    return dateTime == null;
  }
}
