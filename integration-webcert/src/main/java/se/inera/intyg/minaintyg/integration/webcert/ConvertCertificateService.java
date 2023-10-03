package se.inera.intyg.minaintyg.integration.webcert;

import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getTitleText;

import java.util.List;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateResponseDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTypes;

@Service
@RequiredArgsConstructor
public class ConvertCertificateService {

  private final CategoryQuestionOrganizer categoryQuestionOrganizer;
  private final QuestionConverter questionConverter;

  public List<CertificateCategory> convert(CertificateResponseDTO response) {
    final var organizedByCategoryData = categoryQuestionOrganizer.organize(
        getCertificateDataElements(response)
    );

    return organizedByCategoryData.stream()
        .map(this::toCertificateCategory)
        .toList();
  }

  private static List<CertificateDataElement> getCertificateDataElements(
      CertificateResponseDTO response) {
    return response.getCertificate().getData().values().stream().toList();
  }

  private CertificateCategory toCertificateCategory(List<CertificateDataElement> elements) {
    return CertificateCategory.builder()
        .title(getTitleText(elements.get(0)))
        .questions(
            elements.stream()
                .filter(removeCategory())
                .map(questionConverter::convert)
                .toList()
        )
        .build();
  }

  private static Predicate<CertificateDataElement> removeCategory() {
    return element -> !element.getConfig().getType().equals(CertificateDataConfigTypes.CATEGORY);
  }
}
