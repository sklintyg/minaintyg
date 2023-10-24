package se.inera.intyg.minaintyg.certificate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;
import se.inera.intyg.minaintyg.certificate.service.dto.SendCertificateRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.SendCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.SendCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;
import se.inera.intyg.minaintyg.user.UserService;

@Service
@RequiredArgsConstructor
public class SendCertificateService {

  private final MonitoringLogService monitoringLogService;
  private final GetCertificateIntegrationService getCertificateIntegrationService;
  private final SendCertificateIntegrationService sendCertificateIntegrationService;
  private final UserService userService;

  public void send(SendCertificateRequest request) {
    final var user = userService.getLoggedInUser().orElseThrow();
    final var certificateResponse = getCertificate(request.getCertificateId());
    final var recipient = certificateResponse.getCertificate().getMetadata().getRecipient();

    sendCertificate(request, user, certificateResponse.getCertificate());

    monitoringLogService.logCertificateSent(
        request.getCertificateId(),
        certificateResponse.getCertificate().getMetadata().getType().getId(),
        recipient.getId()
    );
  }

  private void sendCertificate(
      SendCertificateRequest request,
      MinaIntygUser user,
      Certificate certificate
  ) {

    validateAction(certificate);

    sendCertificateIntegrationService.send(
        SendCertificateIntegrationRequest
            .builder()
            .certificateId(request.getCertificateId())
            .patientId(user.getPersonId())
            .recipient(certificate.getMetadata().getRecipient().getId())
            .build()
    );
  }

  private GetCertificateIntegrationResponse getCertificate(String certificateId) {
    return getCertificateIntegrationService.get(
        GetCertificateIntegrationRequest
            .builder()
            .certificateId(certificateId)
            .build()
    );
  }

  private static boolean isReplaced(Certificate certificate) {
    final var statuses = certificate.getMetadata().getStatuses();
    return statuses != null
        && statuses.stream().anyMatch(status -> status == CertificateStatusType.REPLACED);
  }

  private void validateAction(Certificate certificate) {
    final var recipient = certificate.getMetadata().getRecipient();

    if (isReplaced(certificate)) {
      throw new IllegalStateException("Cannot send replaced certificate");
    }

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
