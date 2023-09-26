package se.inera.intyg.minaintyg.integration.webcert;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.webcert.client.GetCertificateFromWebcertService;

@Service
@RequiredArgsConstructor
public class WebcertCertificateIntegrationService implements GetCertificateIntegrationService {

  private final GetCertificateFromWebcertService getCertificateFromWebcertService;
  private final ConvertCertificateService convertCertificateService;

  @Override
  public GetCertificateIntegrationResponse get(GetCertificateIntegrationRequest request) {
    validateRequest(request);
    final var response = getCertificateFromWebcertService.get(request);
    final var certificate = convertCertificateService.convert(response);
    return GetCertificateIntegrationResponse.builder()
        .build();
  }

  private void validateRequest(GetCertificateIntegrationRequest request) {
    if (request == null || request.getCertificateId() == null || request.getCertificateId()
        .isEmpty()) {
      throw new IllegalArgumentException(
          "Valid request was not provided, must contain certificate id");
    }
  }
}