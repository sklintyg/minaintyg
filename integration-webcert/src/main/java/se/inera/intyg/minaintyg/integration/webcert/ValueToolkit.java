package se.inera.intyg.minaintyg.integration.webcert;

import java.util.List;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataTextValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateList;

public final class ValueToolkit {

  private static final String NOT_PROVIDED = "Ej angivet";
  private static final String TRUE_BOOLEAN = "Ja";
  private static final String FALSE_BOOLEAN = "Nej";

  private ValueToolkit() {
    throw new IllegalStateException("Utility class");
  }

  public static String getTitleText(CertificateDataElement element) {
    if (element.getConfig().getText() == null || element.getConfig().getText().isEmpty()) {
      return null;
    }
    return element.getConfig().getText();
  }

  public static CertificateQuestionValueText getValueBoolean(CertificateDataValue element) {
    final var value = ((CertificateDataValueBoolean) element).getSelected();
    if (value == null) {
      return CertificateQuestionValueText.builder()
          .value(NOT_PROVIDED)
          .build();
    }
    return CertificateQuestionValueText.builder()
        .value(value ? TRUE_BOOLEAN : FALSE_BOOLEAN)
        .build();
  }

  public static CertificateQuestionValueText getValueText(CertificateDataValue element) {
    final var value = ((CertificateDataTextValue) element).getText();
    return CertificateQuestionValueText.builder()
        .value(value != null ? value : NOT_PROVIDED)
        .build();
  }

  public static List<CertificateQuestion> getValueDateListSubQuestions(
      CertificateDataElement element) {
    if (!(element.getConfig() instanceof CertificateDataConfigCheckboxMultipleDate)) {
      return null;
    }
    final var certificateDataValueDates = ((CertificateDataValueDateList) element.getValue()).getList();
    final var certificateDataConfigDates = ((CertificateDataConfigCheckboxMultipleDate) element.getConfig()).getList();
    return certificateDataConfigDates.stream()
        .map(config -> toDateListSubquestion(certificateDataValueDates, config))
        .toList();
  }

  private static CertificateQuestion toDateListSubquestion(
      List<CertificateDataValueDate> certificateDataValueDates, CheckboxMultipleDate config) {
    return CertificateQuestion.builder()
        .label(config.getLabel())
        .value(getValueIfPresent(certificateDataValueDates, config))
        .build();
  }

  private static CertificateQuestionValueText getValueIfPresent(
      List<CertificateDataValueDate> certificateDataValueDates,
      CheckboxMultipleDate checkboxMultipleDate) {
    final var matchingValue = certificateDataValueDates.stream()
        .filter(element -> element.getId().equals(checkboxMultipleDate.getId()))
        .toList();

    return CertificateQuestionValueText.builder()
        .value(matchingValue.isEmpty() ? NOT_PROVIDED : matchingValue.get(0).getDate().toString())
        .build();
  }
}
