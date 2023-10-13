package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter.NOT_PROVIDED_VALUE;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigDiagnoses;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.DiagnosesTerminology;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosis;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosisList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

class DiagnosisListValueConverterTest {

  private static final String TERMINOLOGY_ID_ONE = "TERMINOLOGY_ID_ONE";
  private static final String TERMINOLOGY_LABEL_ONE = "TERMINOLOGY_LABEL_ONE";
  public static final String TERMINOLOGY_ID_TWO = "TERMINOLOGY_ID_TWO";
  public static final String TERMINOLOGY_LABEL_TWO = "TERMINOLOGY_LABEL_TWO";
  public static final String TERMINOLOGY_ID_THREE = "TERMINOLOGY_ID_THREE";
  private static final String CODE_ONE = "CODE_ONE";
  private static final String CODE_ONE_DESCRIPTION = "CODE_ONE_DESCRIPTION";
  private static final String CODE_TWO = "CODE_TWO";
  private static final String CODE_TWO_DESCRIPTION = "CODE_TWO_DESCRIPTION";

  private final ValueConverter diagnosisListValueConverter = new DiagnosisListValueConverter();

  @Test
  void shallReturnDiagnosisListValueType() {
    assertEquals(CertificateDataValueType.DIAGNOSIS_LIST, diagnosisListValueConverter.getType());
  }

  @Test
  void shallReturnNotProvidedValueIfNull() {
    final var element = CertificateDataElement.builder()
        .value(
            CertificateDataValueDiagnosisList.builder()
                .build()
        )
        .build();

    final var actualValue = diagnosisListValueConverter.convert(element);
    assertEquals(NOT_PROVIDED_VALUE, actualValue);
  }

  @Test
  void shallReturnOneDiagnosisWithTerminologyOne() {
    final var expectedResult = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings(TERMINOLOGY_LABEL_ONE, "")
        )
        .values(
            createValues(
                createValue(CODE_TWO, CODE_TWO_DESCRIPTION)
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createConfig()
        )
        .value(
            createDiagnosisList(
                createDiagnosis(TERMINOLOGY_ID_ONE, CODE_TWO, CODE_TWO_DESCRIPTION)
            )
        )
        .build();

    final var result = diagnosisListValueConverter.convert(element);
    assertEquals(expectedResult, result);
  }

  @Test
  void shallReturnOneDiagnosisWithTerminologyTwo() {
    final var expectedResult = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings(TERMINOLOGY_LABEL_TWO, "")
        )
        .values(
            createValues(
                createValue(CODE_TWO, CODE_TWO_DESCRIPTION)
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createConfig()
        )
        .value(
            createDiagnosisList(
                createDiagnosis(TERMINOLOGY_ID_TWO, CODE_TWO, CODE_TWO_DESCRIPTION)
            )
        )
        .build();

    final var result = diagnosisListValueConverter.convert(element);
    assertEquals(expectedResult, result);
  }

  @Test
  void shallReturnOneDiagnosisWithMissingTerminology() {
    final var expectedResult = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings(TERMINOLOGY_ID_THREE, "")
        )
        .values(
            createValues(
                createValue(CODE_TWO, CODE_TWO_DESCRIPTION)
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createConfig()
        )
        .value(
            createDiagnosisList(
                createDiagnosis(TERMINOLOGY_ID_THREE, CODE_TWO, CODE_TWO_DESCRIPTION)
            )
        )
        .build();

    final var result = diagnosisListValueConverter.convert(element);
    assertEquals(expectedResult, result);
  }

  @Test
  void shallReturnManyDiagnoses() {
    final var expectedResult = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings(TERMINOLOGY_LABEL_ONE, "")
        )
        .values(
            createValues(
                createValue(CODE_TWO, CODE_TWO_DESCRIPTION),
                createValue(CODE_ONE, CODE_ONE_DESCRIPTION)
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createConfig()
        )
        .value(
            createDiagnosisList(
                createDiagnosis(TERMINOLOGY_ID_ONE, CODE_TWO, CODE_TWO_DESCRIPTION),
                createDiagnosis(TERMINOLOGY_ID_ONE, CODE_ONE, CODE_ONE_DESCRIPTION)
            )
        )
        .build();

    final var result = diagnosisListValueConverter.convert(element);
    assertEquals(expectedResult, result);
  }

  private static CertificateDataConfigDiagnoses createConfig() {
    return CertificateDataConfigDiagnoses.builder()
        .terminology(
            List.of(
                DiagnosesTerminology.builder()
                    .id(TERMINOLOGY_ID_ONE)
                    .label(TERMINOLOGY_LABEL_ONE)
                    .build(),

                DiagnosesTerminology.builder()
                    .id(TERMINOLOGY_ID_TWO)
                    .label(TERMINOLOGY_LABEL_TWO)
                    .build()
            )
        )
        .build();
  }

  private static CertificateDataValueDiagnosisList createDiagnosisList(
      CertificateDataValueDiagnosis... diagnosis) {
    return CertificateDataValueDiagnosisList.builder()
        .list(
            List.of(
                diagnosis
            )
        )
        .build();
  }

  private static CertificateDataValueDiagnosis createDiagnosis(String terminologyId,
      String diagnosisCode, String diagnosisDescription) {
    return CertificateDataValueDiagnosis.builder()
        .terminology(terminologyId)
        .code(diagnosisCode)
        .description(diagnosisDescription)
        .build();
  }

  private static List<String> createHeadings(String... headings) {
    return List.of(headings);
  }

  @SafeVarargs
  private static List<List<String>> createValues(List<String>... values) {
    return List.of(values);
  }

  private static List<String> createValue(String... values) {
    return List.of(values);
  }
}