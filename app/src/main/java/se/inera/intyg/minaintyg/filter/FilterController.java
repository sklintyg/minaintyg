package se.inera.intyg.minaintyg.filter;

import static se.inera.intyg.minaintyg.logging.MdcLogConstants.EVENT_TYPE_ACCESSED;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.certificate.dto.CertificateListFilterResponseDTO;
import se.inera.intyg.minaintyg.certificate.service.GetCertificateListFilterService;
import se.inera.intyg.minaintyg.logging.PerformanceLogging;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FilterController {

  private final GetCertificateListFilterService getCertificateListFilterService;

  @GetMapping("/filters")
  @PerformanceLogging(eventAction = "retrieve-certificate-filter", eventType = EVENT_TYPE_ACCESSED)
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
