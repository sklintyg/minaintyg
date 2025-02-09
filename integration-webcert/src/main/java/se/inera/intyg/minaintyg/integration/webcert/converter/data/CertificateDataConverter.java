package se.inera.intyg.minaintyg.integration.webcert.converter.data;

import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter.NOT_PROVIDED;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter.TECHNICAL_ERROR;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElementStyleEnum;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigHeader;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;
import se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter;

@Component
public class CertificateDataConverter {

  private final Map<CertificateDataValueType, ValueConverter> valueConverterMap;

  public CertificateDataConverter(List<ValueConverter> valueConverters) {
    valueConverterMap = valueConverters.stream().collect(
        Collectors.toMap(ValueConverter::getType, Function.identity())
    );
  }

  public List<CertificateCategory> convert(List<CertificateDataElement> certificateDataElements) {
    final var parentQuestionMap = certificateDataElements.stream()
        .filter(Predicate.not(this::elementIsCategory))
        .collect(
            Collectors.groupingBy(
                CertificateDataElement::getParent,
                Collectors.mapping(
                    Function.identity(),
                    Collectors.toList()
                )
            )
        );

    return certificateDataElements.stream()
        .filter(this::elementIsCategory)
        .sorted(Comparator.comparingInt(CertificateDataElement::getIndex))
        .map(element -> toCertificateCategory(element, parentQuestionMap))
        .toList();
  }

  private CertificateCategory toCertificateCategory(CertificateDataElement category,
      Map<String, List<CertificateDataElement>> parentQuestionMap) {
    return CertificateCategory.builder()
        .title(toTitle(category))
        .questions(
            toCertificateQuestions(category, parentQuestionMap)
        )
        .build();
  }

  private List<CertificateQuestion> toCertificateQuestions(CertificateDataElement element,
      Map<String, List<CertificateDataElement>> parentQuestionMap) {
    return parentQuestionMap.getOrDefault(element.getId(), Collections.emptyList()).stream()
        .filter(notHidden())
        .filter(notMessage())
        .sorted(Comparator.comparingInt(CertificateDataElement::getIndex))
        .map(question ->
            CertificateQuestion.builder()
                .title(toTitle(question))
                .label(toLabel(question))
                .header(toHeader(question))
                .value(
                    toValue(
                        question,
                        parentQuestionMap.getOrDefault(question.getId(), Collections.emptyList())
                    )
                )
                .subQuestions(
                    toCertificateQuestions(question, parentQuestionMap)
                )
                .build()
        )
        .toList();
  }

  public static String toTitle(CertificateDataElement element) {
    return getString(element.getConfig().getText());
  }

  private static String toLabel(CertificateDataElement element) {
    return getString(element.getConfig().getLabel());
  }

  private static String toHeader(CertificateDataElement element) {
    return getString(element.getConfig().getHeader());
  }

  private static String getString(String text) {
    return text == null || text.isEmpty() ? null : text;
  }

  public CertificateQuestionValue toValue(CertificateDataElement element,
      List<CertificateDataElement> subQuestions) {
    if (headerElement(element)) {
      return CertificateQuestionValueText.builder().build();
    }

    if (missingValue(element)) {
      return CertificateQuestionValueText.builder()
          .value(NOT_PROVIDED)
          .build();
    }

    if (missingConverter(element)) {
      return CertificateQuestionValueText.builder()
          .value(TECHNICAL_ERROR)
          .build();
    }

    final var valueConverter = valueConverterMap.get(valueType(element));
    return valueConverter.includeSubquestions() ?
        valueConverter.convert(element, subQuestions) :
        valueConverter.convert(element);
  }

  private static boolean headerElement(CertificateDataElement element) {
    return element.getConfig() instanceof CertificateDataConfigHeader;
  }

  private static CertificateDataValueType valueType(CertificateDataElement element) {
    return element.getValue() != null ? element.getValue().getType() : null;
  }

  private static boolean missingValue(CertificateDataElement element) {
    return valueType(element) == null;
  }

  private boolean missingConverter(CertificateDataElement element) {
    return !valueConverterMap.containsKey(valueType(element));
  }

  private boolean elementIsCategory(CertificateDataElement element) {
    return element.getConfig().getType().equals(CertificateDataConfigType.CATEGORY);
  }

  private static Predicate<CertificateDataElement> notHidden() {
    return element -> !CertificateDataElementStyleEnum.HIDDEN.equals(element.getStyle());
  }

  private static Predicate<CertificateDataElement> notMessage() {
    return element -> !element.getConfig().getType().equals(CertificateDataConfigType.UE_MESSAGE);
  }
}