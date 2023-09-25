package se.inera.intyg.minaintyg.integration.webcert;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCompleteCertificateService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateResponse;
import se.inera.intyg.minaintyg.integration.webcert.client.GetCertificateFromWebcertService;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateResponseDTO;


@Service
@RequiredArgsConstructor
public class WebcertCertificateIntegrationService implements GetCompleteCertificateService {

  private final GetCertificateFromWebcertService getCertificateFromWebcertService;

  @Override
  public CertificateResponse get(CertificateRequest request) {
    validateRequest(request);
    try {
      final var response = getCertificateFromWebcertService.get(request);
      return CertificateResponse
          .builder()
          .certificate(convertCertificate(response))
          .build();
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  private Certificate convertCertificate(CertificateResponseDTO response) {
    return Certificate.builder().build();
  }

  private void validateRequest(CertificateRequest request) {
    if (request == null || request.getCertificateId() == null || request.getCertificateId()
        .isEmpty()) {
      throw new IllegalArgumentException(
          "Valid request was not provided, must contain certificate id");
    }
  }
}
