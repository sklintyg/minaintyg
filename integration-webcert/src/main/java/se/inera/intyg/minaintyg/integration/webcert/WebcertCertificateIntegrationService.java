package se.inera.intyg.minaintyg.integration.webcert;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateText;
import se.inera.intyg.minaintyg.integration.common.IllegalCertificateAccessException;
import se.inera.intyg.minaintyg.integration.webcert.client.GetCertificateFromWebcertService;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateResponseDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.Patient;
import se.inera.intyg.minaintyg.integration.webcert.converter.availablefunction.AvailableFunctionConverter;
import se.inera.intyg.minaintyg.integration.webcert.converter.data.CertificateDataConverter;
import se.inera.intyg.minaintyg.integration.webcert.converter.metadata.MetadataConverter;
import se.inera.intyg.minaintyg.integration.webcert.converter.text.CertificateTextConverter;

@Service
@RequiredArgsConstructor
public class WebcertCertificateIntegrationService implements GetCertificateIntegrationService {

  private final GetCertificateFromWebcertService getCertificateFromWebcertService;
  private final MetadataConverter metadataConverter;
  private final CertificateDataConverter certificateDataConverter;
  private final AvailableFunctionConverter availableFunctionConverter;
  private final CertificateTextConverter certificateTextConverter;

  @Override
  public GetCertificateIntegrationResponse get(GetCertificateIntegrationRequest request) {
    validateRequest(request);
    final var response = getCertificateFromWebcertService.get(request);
    validateResponse(request, response);
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
            availableFunctionConverter.convert(response.getAvailableFunctions())
        )
        .texts(getTexts(response))
        .build();
  }

  private List<CertificateText> getTexts(CertificateResponseDTO response) {
    return response.getTexts() == null ? Collections.emptyList() :
        response.getTexts().stream()
            .map(certificateTextConverter::convert)
            .toList();
  }

  private void validateRequest(GetCertificateIntegrationRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Request invalid - was null");
    }

    if (request.getCertificateId() == null || request.getCertificateId().isEmpty()) {
      throw new IllegalArgumentException("Request invalid - missing certificateId");
    }

    if (request.getPersonId() == null || request.getPersonId().isEmpty()) {
      throw new IllegalArgumentException("Request invalid - missing personId");
    }
  }

  private static void validateResponse(GetCertificateIntegrationRequest request,
      CertificateResponseDTO response) {
    final var certificate = response.getCertificate();
    if (certificate == null || certificate.getData() == null || certificate.getData().isEmpty()) {
      throw new IllegalArgumentException(
          "Certificate was not found, certificateId: " + request.getCertificateId());
    }

    if (isInvalidPersonId(request.getPersonId(), certificate.getMetadata().getPatient())) {
      throw new IllegalCertificateAccessException(
          "PersonId does not match for certificate '%s'".formatted(request.getCertificateId())
      );
    }
  }

  private static boolean isInvalidPersonId(String personId, Patient patient) {
    return !personId.replace("-", "").equalsIgnoreCase(
        patient.getPersonId().getId().replace("-", "")
    );
  }
}