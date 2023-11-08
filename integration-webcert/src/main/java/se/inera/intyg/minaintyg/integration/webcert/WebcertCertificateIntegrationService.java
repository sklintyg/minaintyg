package se.inera.intyg.minaintyg.integration.webcert;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.webcert.client.GetCertificateFromWebcertService;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateResponseDTO;
import se.inera.intyg.minaintyg.integration.webcert.converter.data.CertificateDataConverter;
import se.inera.intyg.minaintyg.integration.webcert.converter.metadata.MetadataConverter;

@Service
@RequiredArgsConstructor
public class WebcertCertificateIntegrationService implements GetCertificateIntegrationService {

  private final GetCertificateFromWebcertService getCertificateFromWebcertService;
  private final MetadataConverter metadataConverter;
  private final CertificateDataConverter certificateDataConverter;

  @Override
  public GetCertificateIntegrationResponse get(GetCertificateIntegrationRequest request) {
    validateRequest(request);
    final var response = getCertificateFromWebcertService.get(request);

    if (validateResponse(response)) {
      throw new IllegalArgumentException(
          "Certificate was not found, certificateId: " + request.getCertificateId());
    }

    return GetCertificateIntegrationResponse.builder()
        .certificate(
            Certificate.builder()
                .metadata(
                    metadataConverter.convert(response.getCertificate().getMetadata())
                )
                .categories(
                    certificateDataConverter.convert(
                        response.getCertificate().getData().values().stream().toList()
                    )
                )
                .build()
        )
        .availableFunctions(
            response.getAvailableFunctions() == null
                ? Collections.emptyList()
                : response.getAvailableFunctions()
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