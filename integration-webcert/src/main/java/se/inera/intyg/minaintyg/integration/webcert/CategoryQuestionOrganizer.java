package se.inera.intyg.minaintyg.integration.webcert;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTypes;

@Component
public class CategoryQuestionOrganizer {

  public Map<CertificateDataElement, List<CertificateDataElement>> organize(
      List<CertificateDataElement> certificateDataElements) {

    final var categoryMap = certificateDataElements.stream()
        .filter(CategoryQuestionOrganizer::elementIsCategory)
        .flatMap(category ->
            certificateDataElements.stream()
                .filter(question -> category.getId().equalsIgnoreCase(question.getParent()))
                .map(question -> new SimpleEntry<>(category, question))
        )
        .collect(
            Collectors.groupingBy(
                SimpleEntry::getKey,
                Collectors.mapping(
                    SimpleEntry::getValue,
                    Collectors.toList()
                )
            )
        );

    categoryMap.values().forEach(questions ->
        questions.sort(Comparator.comparingInt(CertificateDataElement::getIndex))
    );

    return categoryMap;
  }

  private static boolean elementIsCategory(CertificateDataElement element) {
    return element.getConfig().getType().equals(CertificateDataConfigTypes.CATEGORY);
  }
}
