package se.inera.intyg.minaintyg.certificate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRecipientRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.SendCertificateRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.SendCertificateResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRecipient;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;

@Service
@RequiredArgsConstructor
public class SendCertificateService {

  private final MonitoringLogService monitoringLogService;
  private final GetCertificateRecipientService getCertificateRecipientService;

  public SendCertificateResponse send(SendCertificateRequest request) {
    final var response = getCertificateRecipientService.get(
        GetCertificateRecipientRequest
            .builder()
            .certificateType(request.getCertificateType())
            .build()
    );

    validateAction(response.getCertificateRecipient());

    monitoringLogService.logCertificateSent(
        request.getCertificateId(),
        request.getCertificateType(),
        request.getRecipient()
    );

    return SendCertificateResponse
        .builder()
        .build();
  }

  private void validateAction(CertificateRecipient recipient) {
    if (recipient == null) {
      throw new IllegalStateException("Cannot send certificate if no recipient");
    }

    if (recipient.getId() == null || recipient.getId().isEmpty() || recipient.getId().isBlank()) {
      throw new IllegalStateException("Cannot send certificate with invalid recipient");
    }

    if (recipient.getSent() != null) {
      throw new IllegalStateException("Cannot send already sent certificate");
    }
  }
}
