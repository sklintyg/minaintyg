package se.inera.intyg.minaintyg.integration.webcert.converter.data;

import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter.NOT_PROVIDED;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter.TECHNICAL_ERROR;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueToolkit.getValueDateListSubQuestions;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;
import se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter;

@Component
public class QuestionConverter {

  private final Map<CertificateDataValueType, ValueConverter> valueConverterMap;

  public QuestionConverter(List<ValueConverter> valueConverters) {
    valueConverterMap = valueConverters.stream().collect(
        Collectors.toMap(ValueConverter::getType, Function.identity())
    );
  }

  public CertificateQuestion convert(CertificateDataElement element) {
    // TODO: Refactor DateList, but we first need to introduce a new CertificateQuestionValueType instead of creating subquestions
    if (CertificateDataValueType.DATE_LIST.equals(valueType(element))) {
      return convertDateList(element);
    }

    final var certificateQuestionBuilder = CertificateQuestion.builder()
        .title(getTitleText(element))
        .label(getLabelText(element));

    if (missingValue(element)) {
      return certificateQuestionBuilder
          .value(
              CertificateQuestionValueText.builder()
                  .value(NOT_PROVIDED)
                  .build()
          )
          .build();
    }

    if (missingConverter(element)) {
      return certificateQuestionBuilder
          .value(
              CertificateQuestionValueText.builder()
                  .value(TECHNICAL_ERROR)
                  .build()
          )
          .build();
    }

    return certificateQuestionBuilder
        .value(
            valueConverterMap.get(valueType(element)).convert(element)
        )
        .build();
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

  public CertificateQuestion convertDateList(CertificateDataElement element) {
    final var certificateQuestionBuilder = CertificateQuestion.builder();
    certificateQuestionBuilder.subQuestions(
        getValueDateListSubQuestions(element.getValue(), element.getConfig()));
    certificateQuestionBuilder.value(CertificateQuestionValueText.builder().build());

    return certificateQuestionBuilder
        .title(getTitleText(element))
        .label(getLabelText(element))
        .build();
  }

  private static String getTitleText(CertificateDataElement element) {
    if (element.getConfig().getText() == null || element.getConfig().getText().isEmpty()) {
      return null;
    }
    return element.getConfig().getText();
  }

  private static String getLabelText(CertificateDataElement element) {
    if (element.getConfig().getLabel() == null || element.getConfig().getLabel().isEmpty()) {
      return null;
    }
    return element.getConfig().getLabel();
  }
}
