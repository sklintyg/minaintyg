package se.inera.intyg.minaintyg.integration.webcert.converter.data;

import static se.inera.intyg.minaintyg.integration.webcert.converter.data.ValueToolkit.getLabelText;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.ValueToolkit.getTitleText;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.ValueToolkit.getValueBoolean;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.ValueToolkit.getValueCode;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.ValueToolkit.getValueCodeList;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.ValueToolkit.getValueDateListSubQuestions;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.ValueToolkit.getValueDateRangeList;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.ValueToolkit.getValueDiagnosisList;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.ValueToolkit.getValueIcf;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.ValueToolkit.getValueText;

import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataIcfValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataTextValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCodeList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateRangeList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosisList;

@Component
public class QuestionConverter {

  public CertificateQuestion convert(CertificateDataElement element) {
    final var certificateQuestionBuilder = CertificateQuestion.builder();
    if (element.getValue() instanceof CertificateDataTextValue) {
      certificateQuestionBuilder.value(getValueText(element.getValue()));
    }
    if (element.getValue() instanceof CertificateDataValueBoolean) {
      certificateQuestionBuilder.value(getValueBoolean(element.getValue()));
    }
    if (element.getValue() instanceof CertificateDataValueDateList) {
      certificateQuestionBuilder.subQuestions(
          getValueDateListSubQuestions(element.getValue(), element.getConfig()));
      certificateQuestionBuilder.value(CertificateQuestionValueText.builder().build());
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
}
