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
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.AvailableFunctionType;
import se.inera.intyg.minaintyg.logging.service.MonitoringLogService;
import se.inera.intyg.minaintyg.user.UserService;
import se.inera.intyg.minaintyg.util.AvailableFunctionUtility;

@Service
@RequiredArgsConstructor
public class SendCertificateService {

  private final MonitoringLogService monitoringLogService;
  private final GetCertificateIntegrationService getCertificateIntegrationService;
  private final SendCertificateIntegrationService sendCertificateIntegrationService;
  private final UserService userService;

  public void send(SendCertificateRequest request) {
    final var user = userService.getLoggedInUser().orElseThrow();
    final var certificateResponse = getCertificate(request.getCertificateId(), user.getPersonId());
    final var recipient = certificateResponse.getCertificate().getMetadata().getRecipient();

    sendCertificate(request, user, certificateResponse);

    monitoringLogService.logCertificateSent(
        request.getCertificateId(),
        certificateResponse.getCertificate().getMetadata().getType().getId(),
        recipient.getId()
    );
  }

  private void sendCertificate(
      SendCertificateRequest request,
      MinaIntygUser user,
      GetCertificateIntegrationResponse certificateResponse
  ) {

    validateAction(certificateResponse);

    sendCertificateIntegrationService.send(
        SendCertificateIntegrationRequest
            .builder()
            .certificateId(request.getCertificateId())
            .patientId(user.getPersonId())
            .recipient(certificateResponse.getCertificate().getMetadata().getRecipient().getId())
            .build()
    );
  }

  private GetCertificateIntegrationResponse getCertificate(String certificateId, String personId) {
    return getCertificateIntegrationService.get(
        GetCertificateIntegrationRequest
            .builder()
            .certificateId(certificateId)
            .personId(personId)
            .build()
    );
  }

  private void validateAction(GetCertificateIntegrationResponse response) {
    if (!AvailableFunctionUtility.includesEnabledFunction(
        response.getAvailableFunctions(),
        AvailableFunctionType.SEND_CERTIFICATE)) {
      throw new IllegalStateException("Certificate cannot be sent");
    }
  }
}
