package se.inera.intyg.minaintyg.information;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.information.dto.InformationResponseDTO;
import se.inera.intyg.minaintyg.information.service.GetBannersService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/info")
public class InformationController {

  private final GetBannersService getBannersService;

  @GetMapping()
  public InformationResponseDTO getInformation() {
    final var banners = getBannersService.get();

    return InformationResponseDTO.builder()
        .banners(banners)
        .build();
  }
}
