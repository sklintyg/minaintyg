package se.inera.intyg.minaintyg.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.certificate.dto.CertificateListFilterResponseDTO;
import se.inera.intyg.minaintyg.certificate.service.GetCertificateListFilterService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FilterController {

  private final GetCertificateListFilterService getCertificateListFilterService;

  @GetMapping("/filters")
  public CertificateListFilterResponseDTO getCertificateListFilter() {
    final var response = getCertificateListFilterService.get();

    return CertificateListFilterResponseDTO
        .builder()
        .years(response.getYears())
        .certificateTypes(response.getCertificateTypes())
        .units(response.getUnits())
        .statuses(response.getStatuses())
        .total(response.getTotal())
        .build();
  }
}
