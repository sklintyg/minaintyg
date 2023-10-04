package se.inera.intyg.minaintyg.certificate;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.certificate.dto.CertificateListFilterResponseDTO;
import se.inera.intyg.minaintyg.certificate.dto.CertificateListRequestDTO;
import se.inera.intyg.minaintyg.certificate.dto.CertificateListResponseDTO;
import se.inera.intyg.minaintyg.certificate.dto.CertificateResponseDTO;
import se.inera.intyg.minaintyg.certificate.dto.SendCertificateRequestDTO;
import se.inera.intyg.minaintyg.certificate.service.GetCertificateListFilterService;
import se.inera.intyg.minaintyg.certificate.service.GetCertificateService;
import se.inera.intyg.minaintyg.certificate.service.ListCertificatesService;
import se.inera.intyg.minaintyg.certificate.service.SendCertificateService;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.SendCertificateRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certificate")
public class CertificateController {

  private final ListCertificatesService listCertificatesService;
  private final GetCertificateListFilterService getCertificateListFilterService;
  private final GetCertificateService getCertificateService;
  private final SendCertificateService sendCertificateService;

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
  public CertificateResponseDTO getCertificate(@PathVariable String certificateId) {
    final var response = getCertificateService.get(
        GetCertificateRequest
            .builder()
            .certificateId(certificateId)
            .build()
    );

    return CertificateResponseDTO
        .builder()
        .certificate(response.getCertificate())
        .build();
  }

  @GetMapping("/filters")
  public CertificateListFilterResponseDTO getCertificateListFilter() {
    final var response = getCertificateListFilterService.get();

    return CertificateListFilterResponseDTO
        .builder()
        .years(response.getYears())
        .certificateTypes(response.getCertificateTypes())
        .units(response.getUnits())
        .statuses(response.getStatuses())
        .build();
  }

  @PostMapping("/send")
  public void sendCertificateToRecipient(SendCertificateRequestDTO request) {
    sendCertificateService.send(
        SendCertificateRequest
            .builder()
            .certificateId(request.getCertificateId())
            .build()
    );
  }
}
