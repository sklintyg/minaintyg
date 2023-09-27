package se.inera.intyg.minaintyg.integration.webcert;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.webcert.client.GetCertificateFromWebcertService;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateResponseDTO;


@Service
@RequiredArgsConstructor
public class WebcertCertificateIntegrationIntegrationService implements
    GetCertificateIntegrationService {

  private final GetCertificateFromWebcertService getCertificateFromWebcertService;

  @Override
  public GetCertificateIntegrationResponse get(GetCertificateIntegrationRequest request) {
    validateRequest(request);
    try {
      final var response = getCertificateFromWebcertService.get(request);
      return GetCertificateIntegrationResponse
          .builder()
          .certificate(convertCertificate(response))
          .build();
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  // TODO: Add implementation in other PR
  private Certificate convertCertificate(CertificateResponseDTO response) {
    return Certificate.builder().build();
  }

  private void validateRequest(GetCertificateIntegrationRequest request) {
    if (request == null || request.getCertificateId() == null || request.getCertificateId()
        .isEmpty()) {
      throw new IllegalArgumentException(
          "Valid request was not provided, must contain certificate id");
    }
  }
}
