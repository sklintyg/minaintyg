package se.inera.intyg.minaintyg.certificate;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.certificate.dto.GetCertificateRecipientRequestDTO;
import se.inera.intyg.minaintyg.certificate.dto.GetCertificateRecipientResponseDTO;
import se.inera.intyg.minaintyg.certificate.dto.SendCertificateRequestDTO;
import se.inera.intyg.minaintyg.certificate.dto.SendCertificateResponseDTO;
import se.inera.intyg.minaintyg.certificate.service.GetCertificateRecipientService;
import se.inera.intyg.minaintyg.certificate.service.SendCertificateService;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRecipientRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.SendCertificateRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipient")
public class CertificateRecipientController {

  private final GetCertificateRecipientService getCertificateRecipientService;
  private final SendCertificateService sendCertificateService;

  @PostMapping
  public GetCertificateRecipientResponseDTO getCertificateRecipient(
      GetCertificateRecipientRequestDTO request) {
    final var response = getCertificateRecipientService.get(
        GetCertificateRecipientRequest
            .builder()
            .certificateType(request.getCertificateType())
            .build()
    );

    return GetCertificateRecipientResponseDTO
        .builder()
        .certificateRecipient(response.getCertificateRecipient())
        .build();
  }

  @PostMapping("/send")
  public SendCertificateResponseDTO sendCertificate(SendCertificateRequestDTO request) {
    final var response = sendCertificateService.send(
        SendCertificateRequest
            .builder()
            .certificateId(request.getCertificateId())
            .certificateType(request.getCertificateType())
            .build()
    );

    return SendCertificateResponseDTO
        .builder()
        .sent(response.getSent())
        .build();
  }
}
