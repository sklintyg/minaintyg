package se.inera.intyg.minaintyg.certificate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRecipientRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRecipientResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateRecipientIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateRecipientIntegrationService;

@Service
@RequiredArgsConstructor
public class GetCertificateRecipientService {

  private final GetCertificateRecipientIntegrationService getCertificateRecipientIntegrationService;

  public GetCertificateRecipientResponse get(GetCertificateRecipientRequest request) {
    final var response = getCertificateRecipientIntegrationService.get(
        GetCertificateRecipientIntegrationRequest
            .builder()
            .certificateId(request.getCertificateId())
            .certificateType(request.getCertificateType())
            .build()
    );

    return GetCertificateRecipientResponse
        .builder()
        .certificateRecipient(response.getCertificateRecipient())
        .build();
  }
}
