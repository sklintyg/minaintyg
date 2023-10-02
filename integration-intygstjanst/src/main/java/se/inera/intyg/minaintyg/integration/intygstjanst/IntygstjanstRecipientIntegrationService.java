package se.inera.intyg.minaintyg.integration.intygstjanst;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateRecipientIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateRecipientIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateRecipientIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.GetRecipientFromIntygstjanstService;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;

@Service
@RequiredArgsConstructor
public class IntygstjanstRecipientIntegrationService implements
    GetCertificateRecipientIntegrationService {

  private final GetRecipientFromIntygstjanstService getRecipientFromIntygstjanstService;

  @Override
  public GetCertificateRecipientIntegrationResponse get(
      GetCertificateRecipientIntegrationRequest request) {
    validateRequest(request);
    try {
      final var response = getRecipientFromIntygstjanstService.get(request);
      return GetCertificateRecipientIntegrationResponse
          .builder()
          .certificateRecipient(
              convertRecipient(response.getRecipient())
          )
          .build();
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  // TODO: Extract to converter class
  private CertificateRecipient convertRecipient(CertificateRecipientDTO recipient) {
    return CertificateRecipient
        .builder()
        .id(recipient.getId())
        .name(recipient.getName())
        .sent(recipient.getSent())
        .build();

  }

  private void validateRequest(GetCertificateRecipientIntegrationRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Valid request was not provided, request cannot be null");
    }

    if (request.getCertificateId() == null || request.getCertificateId().isEmpty()) {
      throw new IllegalArgumentException(
          "Valid request was not provided, certificate id must be defined");
    }

    if (request.getCertificateType() == null || request.getCertificateType().isEmpty()) {
      throw new IllegalArgumentException(
          "Valid request was not provided, certificate type must be defined");
    }
  }
}