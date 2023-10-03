package se.inera.intyg.minaintyg.integration.webcert;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateType;
import se.inera.intyg.minaintyg.integration.webcert.client.GetCertificateFromWebcertService;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateResponseDTO;
import se.inera.intyg.minaintyg.integration.webcert.converter.data.ConvertCertificateService;

@Service
@RequiredArgsConstructor
public class WebcertCertificateIntegrationService implements GetCertificateIntegrationService {

  private final GetCertificateFromWebcertService getCertificateFromWebcertService;
  private final ConvertCertificateService convertCertificateService;

  @Override
  public GetCertificateIntegrationResponse get(GetCertificateIntegrationRequest request) {
    validateRequest(request);
    final var response = getCertificateFromWebcertService.get(request);

    if (validateResponse(response)) {
      throw new RuntimeException(
          "Certificate was not found, certificateId: " + request.getCertificateId());
    }

    final var certificateCategories = convertCertificateService.convert(response.getCertificate());

    return GetCertificateIntegrationResponse.builder()
        .certificate(
            Certificate.builder()
                .metadata(CertificateMetadata.builder()
                    .type(
                        CertificateType.builder()
                            .id(response.getCertificate().getMetadata().getId())
                            .name(response.getCertificate().getMetadata().getName())
                            .build()
                    )
                    .build())
                .categories(certificateCategories)
                .build()
        )
        .build();
  }

  private static boolean validateResponse(CertificateResponseDTO response) {
    return response.getCertificate() == null || response.getCertificate().getData() == null
        || response.getCertificate().getData().isEmpty();
  }

  private void validateRequest(GetCertificateIntegrationRequest request) {
    if (request == null || request.getCertificateId() == null || request.getCertificateId()
        .isEmpty()) {
      throw new IllegalArgumentException(
          "Valid request was not provided, must contain certificate id");
    }
  }
}