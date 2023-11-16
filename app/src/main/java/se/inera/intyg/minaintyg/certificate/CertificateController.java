package se.inera.intyg.minaintyg.certificate;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.certificate.dto.CertificateListRequestDTO;
import se.inera.intyg.minaintyg.certificate.dto.CertificateListResponseDTO;
import se.inera.intyg.minaintyg.certificate.dto.CertificateResponseDTO;
import se.inera.intyg.minaintyg.certificate.service.GetCertificateService;
import se.inera.intyg.minaintyg.certificate.service.ListCertificatesService;
import se.inera.intyg.minaintyg.certificate.service.PrintCertificateService;
import se.inera.intyg.minaintyg.certificate.service.SendCertificateService;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.PrintCertificateRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.SendCertificateRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certificate")
public class CertificateController {

  private static final String CONTENT_DISPOSITION = "Content-Disposition";
  private static final String INLINE = "inline";

  private final ListCertificatesService listCertificatesService;
  private final GetCertificateService getCertificateService;
  private final SendCertificateService sendCertificateService;
  private final PrintCertificateService printCertificateService;

  @PostMapping
  public CertificateListResponseDTO listCertificates(
      @RequestBody CertificateListRequestDTO request) {
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

  @GetMapping("/{certificateId}")
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
        .availableFunctions(response.getAvailableFunctions())
        .build();
  }

  @PostMapping("/{certificateId}/send")
  public void sendCertificateToRecipient(@PathVariable String certificateId) {
    sendCertificateService.send(
        SendCertificateRequest
            .builder()
            .certificateId(certificateId)
            .build()
    );
  }

  @GetMapping(value = "/{certificateId}/pdf/{customizationId}", produces = "application/pdf")
  public ResponseEntity<byte[]> printCertificate(
      @PathVariable String certificateId,
      @PathVariable(required = false) String customizationId) {

    final var response = printCertificateService.print(
        PrintCertificateRequest
            .builder()
            .certificateId(certificateId)
            .customizationId(customizationId)
            .build()
    );

    final var responseHeaders = getHttpHeaders();

    return ResponseEntity.ok()
        .headers(responseHeaders)
        .body(response.getPdfData());
  }

  private static HttpHeaders getHttpHeaders() {
    final var responseHeaders = new HttpHeaders();
    responseHeaders.set(CONTENT_DISPOSITION, INLINE);
    return responseHeaders;
  }
}
