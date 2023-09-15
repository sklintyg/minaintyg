package se.inera.intyg.minaintyg.certificate;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.certificate.service.GetCertificateFilterService;
import se.inera.intyg.minaintyg.certificate.service.ListCertificatesService;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certificate")
public class CertificateController {

  private final ListCertificatesService listCertificatesService;
  private final GetCertificateFilterService getCertificateFilterService;

  @PostMapping("")
  public CertificatesResponseDTO listCertificates(CertificatesRequestDTO request) {
    final var listCertificatesRequest =
        ListCertificatesRequest
            .builder()
            .years(request.getYears())
            .certificateTypes(request.getCertificateTypes())
            .units(request.getUnits())
            .statuses(request.getStatuses())
            .build();

    return CertificatesResponseDTO
        .builder()
        .content(listCertificatesService.get(listCertificatesRequest).getContent())
        .build();
  }

  @GetMapping("/filters")
  public CertificateFilterResponse getFilters() {
    final var response = getCertificateFilterService.get();

    return CertificateFilterResponse
        .builder()
        .years(response.getYears())
        .certificateTypes(response.getCertificateTypes())
        .units(response.getUnits())
        .statuses(response.getStatuses())
        .build();
  }

}
