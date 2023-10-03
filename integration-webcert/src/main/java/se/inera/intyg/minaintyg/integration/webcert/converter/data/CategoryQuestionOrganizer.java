package se.inera.intyg.minaintyg.integration.webcert.converter.data;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTypes;

@Component
public class CategoryQuestionOrganizer {

  public Map<CertificateDataElement, List<CertificateDataElement>> organize(
      List<CertificateDataElement> certificateDataElements) {

    final var categoryMap = getCategoryMap(certificateDataElements);
    final var questionMap = getQuestionMap(certificateDataElements);

    final var categoryQuestionMap = categoryMap.values().stream()
        .flatMap(category ->
            questionMap.values().stream()
                .filter(questionBelongToCategory(category, questionMap))
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

    sortQuestionsInIndexOrder(categoryQuestionMap);

    return categoryQuestionMap;
  }

  private static Map<String, CertificateDataElement> getQuestionMap(
      List<CertificateDataElement> certificateDataElements) {
    return certificateDataElements.stream()
        .filter(Predicate.not(CategoryQuestionOrganizer::elementIsCategory))
        .collect(Collectors.toMap(CertificateDataElement::getId, Function.identity()));
  }

  private static Map<String, CertificateDataElement> getCategoryMap(
      List<CertificateDataElement> certificateDataElements) {
    return certificateDataElements.stream()
        .filter(CategoryQuestionOrganizer::elementIsCategory)
        .collect(Collectors.toMap(CertificateDataElement::getId, Function.identity()));
  }

  private static boolean elementIsCategory(CertificateDataElement element) {
    return element.getConfig().getType().equals(CertificateDataConfigTypes.CATEGORY);
  }

  private static Predicate<CertificateDataElement> questionBelongToCategory(
      CertificateDataElement category, Map<String, CertificateDataElement> questionMap) {
    return question -> category.getId().equalsIgnoreCase(getParent(questionMap, question));
  }

  private static void sortQuestionsInIndexOrder(
      Map<CertificateDataElement, List<CertificateDataElement>> categoryQuestionMap) {
    categoryQuestionMap.values().forEach(questions ->
        questions.sort(Comparator.comparingInt(CertificateDataElement::getIndex))
    );
  }

  private static String getParent(Map<String, CertificateDataElement> questions,
      CertificateDataElement question) {
    final var parentQuestion = questions.get(question.getParent());
    if (parentQuestion != null) {
      return getParent(questions, parentQuestion);
    }
    return question.getParent();
  }
}
