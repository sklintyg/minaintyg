package se.inera.intyg.minaintyg.integrationtest.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.ApplicationDTO;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerPriorityDTO;

public class BannerUtil {

  public static BannerDTO activeBanner(BannerPriorityDTO priority, String message) {
    return BannerDTO.builder()
        .id(UUID.randomUUID())
        .application(ApplicationDTO.MINA_INTYG)
        .createdAt(LocalDateTime.now(ZoneId.systemDefault()))
        .displayFrom(LocalDateTime.now(ZoneId.systemDefault()).minusHours(1))
        .displayTo(LocalDateTime.now(ZoneId.systemDefault()).plusHours(1))
        .message(message)
        .priority(priority)
        .build();
  }
}
