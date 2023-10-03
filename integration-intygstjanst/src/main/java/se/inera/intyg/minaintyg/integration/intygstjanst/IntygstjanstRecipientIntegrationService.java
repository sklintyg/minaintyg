package se.inera.intyg.minaintyg.integration.intygstjanst;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateRecipientIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateRecipientIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateRecipientIntegrationService;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.GetRecipientFromIntygstjanstService;

@Service
@RequiredArgsConstructor
public class IntygstjanstRecipientIntegrationService implements
    GetCertificateRecipientIntegrationService {

  private final GetRecipientFromIntygstjanstService getRecipientFromIntygstjanstService;
  private final CertificateRecipientConverter certificateRecipientConverter;

  @Override
  public GetCertificateRecipientIntegrationResponse get(
      GetCertificateRecipientIntegrationRequest request) {
    validateRequest(request);
    try {
      final var response = getRecipientFromIntygstjanstService.get(request);
      return GetCertificateRecipientIntegrationResponse
          .builder()
          .certificateRecipient(
              certificateRecipientConverter.convert(response.getRecipient())
          )
          .build();
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  private void validateRequest(GetCertificateRecipientIntegrationRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Valid request was not provided, request cannot be null");
    }

    if (request.getCertificateId() == null || request.getCertificateId().isEmpty()) {
      throw new IllegalArgumentException(
          "Valid request was not provided, certificate id must be defined");
    }
  }
}