package se.inera.intyg.minaintyg.certificate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRecipientRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.SendCertificateRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.SendCertificateResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.SendCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.SendCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRecipient;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;

@Service
@RequiredArgsConstructor
public class SendCertificateService {

  private final MonitoringLogService monitoringLogService;
  private final GetCertificateRecipientService getCertificateRecipientService;
  private final SendCertificateIntegrationService sendCertificateIntegrationService;

  public SendCertificateResponse send(SendCertificateRequest request) {
    final var recipientResponse = getCertificateRecipientService.get(
        GetCertificateRecipientRequest
            .builder()
            .certificateId(request.getCertificateId())
            .certificateType(request.getCertificateType())
            .build()
    );

    validateAction(recipientResponse.getCertificateRecipient());

    final var sentResponse = sendCertificateIntegrationService.send(
        SendCertificateIntegrationRequest
            .builder()
            .certificateId(request.getCertificateId())
            .certificateType(request.getCertificateType())
            .recipient(recipientResponse.getCertificateRecipient().getId())
            .build()
    );

    if (sentResponse.getSent() == null) {
      // TODO: Throw error?
      return null;
    }

    monitoringLogService.logCertificateSent(
        request.getCertificateId(),
        request.getCertificateType(),
        recipientResponse.getCertificateRecipient().getId()
    );

    return SendCertificateResponse
        .builder()
        .sent(sentResponse.getSent())
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
