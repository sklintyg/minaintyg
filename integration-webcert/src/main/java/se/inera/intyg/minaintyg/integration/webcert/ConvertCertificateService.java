package se.inera.intyg.minaintyg.integration.webcert;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.GetCertificateResponse;

@Service
@RequiredArgsConstructor
public class ConvertCertificateService {

  private final CategoryQuestionOrganizer categoryQuestionOrganizer;

  public Certificate convert(GetCertificateResponse response) {
    final var certificateDataElements = getCertificateDataElements(response);
    final var categoryWithQuestionsList = categoryQuestionOrganizer.organize(
        certificateDataElements);

    return Certificate.builder()
        .metadata(CertificateMetadata.builder()
            .build())
        .build();
  }

  private static List<CertificateDataElement> getCertificateDataElements(
      GetCertificateResponse response) {
    return response.getData().values().stream().toList();
  }
}
