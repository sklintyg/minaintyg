package se.inera.intyg.minaintyg.certificate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRecipientRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRecipientResponse;
import se.inera.intyg.minaintyg.certificate.service.dto.SendCertificateRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.SendCertificateResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.SendCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.SendCertificateIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.SendCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRecipient;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;
import se.inera.intyg.minaintyg.user.UserService;

@Service
@RequiredArgsConstructor
public class SendCertificateService {

  private final MonitoringLogService monitoringLogService;
  private final GetCertificateRecipientService getCertificateRecipientService;
  private final SendCertificateIntegrationService sendCertificateIntegrationService;
  private final UserService userService;

  public SendCertificateResponse send(SendCertificateRequest request) {
    final var user = userService.getLoggedInUser().orElseThrow();
    final var recipientResponse = getRecipient(request);

    final var sentResponse = sendCertificate(request, user, recipientResponse);
    logCertificateSent(request, recipientResponse);

    return SendCertificateResponse
        .builder()
        .sent(sentResponse.getSent())
        .build();
  }

  private void logCertificateSent(SendCertificateRequest request,
      GetCertificateRecipientResponse recipientResponse) {
    monitoringLogService.logCertificateSent(
        request.getCertificateId(),
        request.getCertificateType(),
        recipientResponse.getCertificateRecipient().getId()
    );
  }

  private SendCertificateIntegrationResponse sendCertificate(SendCertificateRequest request,
      MinaIntygUser user, GetCertificateRecipientResponse recipientResponse) {

    validateAction(recipientResponse.getCertificateRecipient());

    return sendCertificateIntegrationService.send(
        SendCertificateIntegrationRequest
            .builder()
            .certificateId(request.getCertificateId())
            .patientId(user.getPersonId())
            .recipient(recipientResponse.getCertificateRecipient().getId())
            .build()
    );
  }

  private GetCertificateRecipientResponse getRecipient(SendCertificateRequest request) {
    return getCertificateRecipientService.get(
        GetCertificateRecipientRequest
            .builder()
            .certificateId(request.getCertificateId())
            .certificateType(request.getCertificateType())
            .build()
    );
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
