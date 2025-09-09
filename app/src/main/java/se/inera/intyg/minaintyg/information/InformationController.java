package se.inera.intyg.minaintyg.information;

import static se.inera.intyg.minaintyg.logging.MdcLogConstants.EVENT_TYPE_ACCESSED;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.information.config.EnvironmentConfig;
import se.inera.intyg.minaintyg.information.dto.InformationResponseDTO;
import se.inera.intyg.minaintyg.information.service.GetBannersService;
import se.inera.intyg.minaintyg.logging.PerformanceLogging;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/info")
public class InformationController {

  private final GetBannersService getBannersService;
  private final EnvironmentConfig environmentConfig;

  @GetMapping()
  @PerformanceLogging(eventAction = "retrieve-information", eventType = EVENT_TYPE_ACCESSED)
  public InformationResponseDTO getInformation() {
    final var banners = getBannersService.get();

    return InformationResponseDTO.builder()
        .banners(banners)
        .environment(environmentConfig.getEnvironmentType())
        .build();
  }
}
