package se.inera.intyg.minaintyg.integration.webcert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTypes;

@Component
public class CategoryQuestionOrganizer {

  public List<CategoryWithQuestions> organize(
      List<CertificateDataElement> certificateDataElements) {
    final var elementsSortedByIndex = getElementsSortedByIndex(certificateDataElements);
    final var categoryWithQuestions = new ArrayList<CategoryWithQuestions>();

    elementsSortedByIndex.forEach(certificateDataElement -> {
      if (elementIsCategory(certificateDataElement)) {
        categoryWithQuestions.add(new CategoryWithQuestions(certificateDataElement));
      }
      if (categoryWithQuestions.isEmpty()) {
        return;
      }
      categoryWithQuestions.get(categoryWithQuestions.size() - 1)
          .addQuestion(certificateDataElement);
    });

    return categoryWithQuestions;
  }

  private static List<CertificateDataElement> getElementsSortedByIndex(
      List<CertificateDataElement> certificateDataElements) {
    return certificateDataElements.stream()
        .sorted(Comparator.comparingInt(CertificateDataElement::getIndex))
        .toList();
  }

  private static boolean elementIsCategory(CertificateDataElement element) {
    return element.getConfig().getType().equals(CertificateDataConfigTypes.CATEGORY);
  }
}
