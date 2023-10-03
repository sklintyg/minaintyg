package se.inera.intyg.minaintyg.integration.webcert.converter.data;

import java.util.List;
import java.util.Map;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigDiagnoses;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioMultipleCodeOptionalDropdown;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.DiagnosesTerminology;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.RadioMultipleCodeOptionalDropdown;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataIcfValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataTextValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCodeList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosisList;

public final class ValueToolkit {

  private static final String NOT_PROVIDED = "Ej angivet";
  private static final String TRUE_BOOLEAN = "Ja";
  private static final String FALSE_BOOLEAN = "Nej";
  private static final Map<String, String> CODE_MAP = Map.ofEntries(
      Map.entry("NUVARANDE_ARBETE", "Nuvarande arbete"),
      Map.entry("ARBETSSOKANDE", "Arbetssökande"),
      Map.entry("FORALDRALEDIG", "Föräldraledighet för vård av barn"),
      Map.entry("STUDIER", "Studier"),
      Map.entry("EJ_AKTUELLT", "Inte aktuellt"),
      Map.entry("ARBETSTRANING", "Arbetsträning"),
      Map.entry("ARBETSANPASSNING", "Arbetsanpassning"),
      Map.entry("SOKA_NYTT_ARBETE", "Söka nytt arbete"),
      Map.entry("BESOK_ARBETSPLATS", "Besök på arbetsplatsen"),
      Map.entry("ERGONOMISK", "Ergonomisk bedömning"),
      Map.entry("HJALPMEDEL", "Hjälpmedel"),
      Map.entry("KONFLIKTHANTERING", "Konflikthantering"),
      Map.entry("KONTAKT_FHV", "Kontakt med företagshälsovård"),
      Map.entry("OMFORDELNING", "Omfördelning av arbetsuppgifter"),
      Map.entry("OVRIGA_ATGARDER", "Övrigt")
  );

  private ValueToolkit() {
    throw new IllegalStateException("Utility class");
  }

  public static String getTitleText(CertificateDataElement element) {
    if (element.getConfig().getText() == null || element.getConfig().getText().isEmpty()) {
      return null;
    }
    return element.getConfig().getText();
  }

  public static String getLabelText(CertificateDataElement element) {
    if (element.getConfig().getLabel() == null || element.getConfig().getLabel().isEmpty()) {
      return null;
    }
    return element.getConfig().getLabel();
  }

  public static CertificateQuestionValueText getValueBoolean(
      CertificateDataValue certificateDataValue) {
    final var value = ((CertificateDataValueBoolean) certificateDataValue).getSelected();
    if (value == null) {
      return notProvidedTextValue();
    }
    return CertificateQuestionValueText.builder()
        .value(value ? TRUE_BOOLEAN : FALSE_BOOLEAN)
        .build();
  }

  public static CertificateQuestionValueText getValueText(
      CertificateDataValue certificateDataValue) {
    final var value = ((CertificateDataTextValue) certificateDataValue).getText();
    return getText(value);
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

  public static CertificateQuestionValueList getValueCodeList(
      CertificateDataValue certificateDataValue) {
    final var value = ((CertificateDataValueCodeList) certificateDataValue).getList();
    if (value == null || value.isEmpty()) {
      return notProvidedValueList();
    }
    return CertificateQuestionValueList.builder()
        .values(
            value.stream()
                .map(CertificateDataValueCode::getCode)
                .map(ValueToolkit::codeToString)
                .toList()
        )
        .build();
  }

  public static CertificateQuestionValueTable getValueDiagnosisList(
      CertificateDataValue certificateDataValue, CertificateDataConfig certificateDataConfig) {
    final var diagnosesTerminologies = ((CertificateDataConfigDiagnoses) certificateDataConfig).getTerminology();
    final var certificateDataValueDiagnoses = ((CertificateDataValueDiagnosisList) certificateDataValue).getList();

    final var headings = diagnosesTerminologies.stream()
        .map(DiagnosesTerminology::getLabel)
        .toList();

    final var values = certificateDataValueDiagnoses.stream()
        .map(diagnosis -> List.of(diagnosis.getCode(), diagnosis.getDescription()))
        .toList();

    return buildQuestionValueTable(headings, values);
  }

  public static CertificateQuestionValueText getValueIcf(
      CertificateDataValue certificateDataValue) {
    final var value = ((CertificateDataIcfValue) certificateDataValue).getText();
    return getText(value);
  }

  public static CertificateQuestionValueTable getValueDateRangeList(
      CertificateDataValue certificateDataValue, CertificateDataConfig certificateDataConfig) {
    return CertificateQuestionValueTable.builder()
        .headings(List.of(
            "Not yet implemented",
            "Not yet implemented"
        ))
        .values(
            List.of(
                List.of(
                    "Not yet implemented",
                    "Not yet implemented"
                )
            ))
        .build();
  }

  public static CertificateQuestionValueText getValueCode(
      CertificateDataValue certificateDataValue, CertificateDataConfig certificateDataConfig) {
    if (!(certificateDataConfig instanceof final CertificateDataConfigRadioMultipleCodeOptionalDropdown dataConfig)) {
      return notProvidedTextValue();
    }
    final var dataValue = (CertificateDataValueCode) certificateDataValue;
    final var radioMultipleCodeOptionalDropdownLabel = dataConfig.getList().stream()
        .filter(config -> config.getId().equals(dataValue.getCode()))
        .map(RadioMultipleCodeOptionalDropdown::getLabel)
        .toList();

    return CertificateQuestionValueText.builder()
        .value(radioMultipleCodeOptionalDropdownLabel.isEmpty() ? NOT_PROVIDED
            : radioMultipleCodeOptionalDropdownLabel.get(0))
        .build();
  }

  private static String codeToString(String code) {
    return CODE_MAP.getOrDefault(code, null);
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

  private static CertificateQuestionValueText getText(String value) {
    return CertificateQuestionValueText.builder()
        .value(value != null ? value : NOT_PROVIDED)
        .build();
  }

  private static CertificateQuestionValueText notProvidedTextValue() {
    return CertificateQuestionValueText.builder()
        .value(NOT_PROVIDED)
        .build();
  }

  private static CertificateQuestionValueList notProvidedValueList() {
    return CertificateQuestionValueList.builder()
        .values(
            List.of(NOT_PROVIDED)
        )
        .build();
  }

  private static CertificateQuestionValueTable buildQuestionValueTable(List<String> headings,
      List<List<String>> values) {
    return CertificateQuestionValueTable.builder()
        .headings(headings)
        .values(values)
        .build();
  }
}
