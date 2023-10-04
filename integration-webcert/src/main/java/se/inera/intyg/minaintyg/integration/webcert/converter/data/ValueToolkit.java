package se.inera.intyg.minaintyg.integration.webcert.converter.data;

import java.util.List;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateList;

public final class ValueToolkit {

  private static final String NOT_PROVIDED = "Ej angivet";

  private ValueToolkit() {
    throw new IllegalStateException("Utility class");
  }

  public static List<CertificateQuestion> getValueDateListSubQuestions(
      CertificateDataValue certificateDataValue, CertificateDataConfig certificateDataValueConfig) {
    final var certificateDataValueDates = ((CertificateDataValueDateList) certificateDataValue).getList();
    final var certificateDataConfigDates = ((CertificateDataConfigCheckboxMultipleDate) certificateDataValueConfig).getList();
    if (certificateDataValueDates == null) {
      return List.of(
          CertificateQuestion.builder()
              .value(notProvidedTextValue())
              .build()
      );
    }
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

  private static CertificateQuestionValueText notProvidedTextValue() {
    return CertificateQuestionValueText.builder()
        .value(NOT_PROVIDED)
        .build();
  }
}
