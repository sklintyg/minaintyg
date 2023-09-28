package se.inera.intyg.minaintyg.integration.webcert;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;
import se.inera.intyg.minaintyg.integration.webcert.client.GetCertificateFromWebcertService;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateResponseDTO;

@Service
@RequiredArgsConstructor
public class WebcertCertificateIntegrationService implements GetCertificateIntegrationService {

  private final GetCertificateFromWebcertService getCertificateFromWebcertService;
  private final CategoryQuestionOrganizer categoryQuestionOrganizer;
  private final ConvertCertificateService convertCertificateService;

  @Override
  public GetCertificateIntegrationResponse get(GetCertificateIntegrationRequest request) {
    validateRequest(request);
    final var response = getCertificateFromWebcertService.get(request);

    if (responseContainsNoData(response)) {
      //TODO: Throw exception
    }

    final var organizedByCategoryData = categoryQuestionOrganizer.organize(
        getCertificateDataElements(response)
    );

    final var certificateCategories = convertCertificateService.convert(organizedByCategoryData);

    return GetCertificateIntegrationResponse.builder()
        .certificate(
            Certificate.builder()
                .metadata(CertificateMetadata.builder().build())
                .categories(certificateCategories)
                .build()
        )
        .build();
  }

  private static boolean responseContainsNoData(CertificateResponseDTO response) {
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

  private static List<CertificateDataElement> getCertificateDataElements(
      CertificateResponseDTO response) {
    return response.getCertificate().getData().values().stream().toList();
  }
}