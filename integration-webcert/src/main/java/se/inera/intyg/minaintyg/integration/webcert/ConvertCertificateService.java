package se.inera.intyg.minaintyg.integration.webcert;

import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getLabelText;
import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getTitleText;
import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getValueBoolean;
import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getValueCode;
import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getValueCodeList;
import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getValueDateListSubQuestions;
import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getValueDateRangeList;
import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getValueDiagnosisList;
import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getValueIcf;
import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getValueText;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTypes;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataIcfValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataTextValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCodeList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateRangeList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosisList;

@Service
@RequiredArgsConstructor
public class ConvertCertificateService {


  public List<CertificateCategory> convert(
      List<List<CertificateDataElement>> certificateDataElements) {
    return certificateDataElements.stream()
        .map(this::toCertificateCategory)
        .collect(Collectors.toList());
  }

  private CertificateCategory toCertificateCategory(List<CertificateDataElement> elements) {
    return CertificateCategory.builder()
        .title(getTitleText(elements.get(0)))
        .questions(
            elements.stream()
                .filter(removeCategory())
                .map(this::toCertificateQuestion)
                .collect(Collectors.toList())
        )
        .build();
  }

  private CertificateQuestion toCertificateQuestion(CertificateDataElement element) {
    final var certificateQuestionBuilder = CertificateQuestion.builder();
    if (element.getValue() instanceof CertificateDataTextValue) {
      certificateQuestionBuilder.value(getValueText(element.getValue()));
    }
    if (element.getValue() instanceof CertificateDataValueBoolean) {
      certificateQuestionBuilder.value(getValueBoolean(element.getValue()));
    }
    if (element.getValue() instanceof CertificateDataValueDateList) {
      certificateQuestionBuilder.subQuestions(getValueDateListSubQuestions(element));
    }
    if (element.getValue() instanceof CertificateDataValueCodeList) {
      certificateQuestionBuilder.value(getValueCodeList(element.getValue()));
    }
    if (element.getValue() instanceof CertificateDataValueDiagnosisList) {
      certificateQuestionBuilder.value(
          getValueDiagnosisList(element.getValue(), element.getConfig()));
    }
    if (element.getValue() instanceof CertificateDataIcfValue) {
      certificateQuestionBuilder.value(getValueIcf(element.getValue()));
    }
    if (element.getValue() instanceof CertificateDataValueDateRangeList) {
      //TODO: Not yet implemented
      certificateQuestionBuilder.value(
          getValueDateRangeList(element.getValue(), element.getConfig()));
    }
    if (element.getValue() instanceof CertificateDataValueCode) {
      certificateQuestionBuilder.value(getValueCode(element.getValue(), element.getConfig()));
    }

    return certificateQuestionBuilder
        .title(getTitleText(element))
        .label(getLabelText(element))
        .build();
  }

  private static Predicate<CertificateDataElement> removeCategory() {
    return element -> !element.getConfig().getType().equals(CertificateDataConfigTypes.CATEGORY);
  }
}
