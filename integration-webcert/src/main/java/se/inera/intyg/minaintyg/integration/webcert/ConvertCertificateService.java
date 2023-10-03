package se.inera.intyg.minaintyg.integration.webcert;

import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getTitleText;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateResponseDTO;

@Service
@RequiredArgsConstructor
public class ConvertCertificateService {

  private final CategoryQuestionOrganizer categoryQuestionOrganizer;
  private final QuestionConverter questionConverter;

  public List<CertificateCategory> convert(CertificateResponseDTO response) {
    final var organizedByCategoryData = categoryQuestionOrganizer.organizeAsMap(
        getCertificateDataElements(response)
    );

    return organizedByCategoryData.keySet().stream()
        .map(category -> toCertificateCategory(category, organizedByCategoryData.get(category)))
        .toList();
  }

  private static List<CertificateDataElement> getCertificateDataElements(
      CertificateResponseDTO response) {
    return response.getCertificate().getData().values().stream().toList();
  }

  private CertificateCategory toCertificateCategory(CertificateDataElement category,
      List<CertificateDataElement> elements) {
    return CertificateCategory.builder()
        .title(getTitleText(category))
        .questions(
            elements.stream()
                .map(questionConverter::convert)
                .toList()
        )
        .build();
  }
}
