package se.inera.intyg.minaintyg.certificate;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.certificate.dto.CertificateFilterResponseDTO;
import se.inera.intyg.minaintyg.certificate.dto.CertificateListRequestDTO;
import se.inera.intyg.minaintyg.certificate.dto.CertificateListResponseDTO;
import se.inera.intyg.minaintyg.certificate.dto.CertificateRequestDTO;
import se.inera.intyg.minaintyg.certificate.dto.CertificateResponseDTO;
import se.inera.intyg.minaintyg.certificate.service.GetCertificateFilterService;
import se.inera.intyg.minaintyg.certificate.service.GetCertificateService;
import se.inera.intyg.minaintyg.certificate.service.ListCertificatesService;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certificate")
public class CertificateController {

  private final ListCertificatesService listCertificatesService;
  private final GetCertificateFilterService getCertificateFilterService;
  private final GetCertificateService getCertificateService;

  @PostMapping
  public CertificateListResponseDTO listCertificates(CertificateListRequestDTO request) {
    final var listCertificatesRequest =
        ListCertificatesRequest
            .builder()
            .years(request.getYears())
            .certificateTypes(request.getCertificateTypes())
            .units(request.getUnits())
            .statuses(request.getStatuses())
            .build();

    return CertificateListResponseDTO
        .builder()
        .content(listCertificatesService.get(listCertificatesRequest).getContent())
        .build();
  }

  @PostMapping("/{certificateId}")
  public CertificateResponseDTO getCertificate(CertificateRequestDTO request) {
    final var response = getCertificateService.get(
        GetCertificateRequest
            .builder()
            .certificateId(request.getCertificateId())
            .build()
    );

    return CertificateResponseDTO
        .builder()
        .content(response.getCertificate().getFormattedContent())
        .metadata(response.getCertificate().getMetadata())
        .build();
  }

  @GetMapping("/filters")
  public CertificateFilterResponseDTO getFilters() {
    final var response = getCertificateFilterService.get();

    return CertificateFilterResponseDTO
        .builder()
        .years(response.getYears())
        .certificateTypes(response.getCertificateTypes())
        .units(response.getUnits())
        .statuses(response.getStatuses())
        .build();
  }

}
