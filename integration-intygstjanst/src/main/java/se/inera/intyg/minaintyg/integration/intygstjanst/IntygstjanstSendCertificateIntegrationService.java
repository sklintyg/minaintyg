package se.inera.intyg.minaintyg.integration.intygstjanst;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.SendCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.SendCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.SendCertificateUsingIntygstjanstService;

@Service
@RequiredArgsConstructor
public class IntygstjanstSendCertificateIntegrationService implements
    SendCertificateIntegrationService {

  private final SendCertificateUsingIntygstjanstService sendCertificateUsingIntygstjanstService;

  @Override
  public void send(
      SendCertificateIntegrationRequest request) {
    validateRequest(request);
    try {
      sendCertificateUsingIntygstjanstService.send(request);
    } catch (Exception exception) {
      throw new IllegalStateException(exception);
    }
  }

  private void validateRequest(SendCertificateIntegrationRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Valid request was not provided");
    }

    if (request.getCertificateId() == null || request.getCertificateId().isEmpty()) {
      throw new IllegalArgumentException(
          "Valid request was not provided, must include certificate id");
    }

    if (request.getPatientId() == null || request.getPatientId().isEmpty()) {
      throw new IllegalArgumentException("Valid request was not provided, must include patient id");
    }

    if (request.getRecipient() == null || request.getRecipient().isEmpty()) {
      throw new IllegalArgumentException("Valid request was not provided, must include recipient");
    }
  }
}