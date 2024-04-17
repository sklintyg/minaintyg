package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter.MISSING_LABEL;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter.NOT_PROVIDED_VALUE;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigMedicalInvestigation;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CodeItem;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.MedicalInvestigation;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueMedicalInvestigation;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueMedicalInvestigationList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

class MedicalInvestigationValueConverterTest {

  public static final String INVESTIGATION_TYPE_HEADER = "INVESTIGATION_TYPE_HEADER";
  public static final String DATE_HEADER = "DATE_HEADER";
  public static final String INFORMATION_SOURCE_HEADER = "INFORMATION_SOURCE_HEADER";
  public static final String DATE_ID = "DATE_ID";
  public static final String DATE_ONE = "2023-01-01";
  public static final String DATE_TWO = "2023-02-01";
  public static final String DATE_THREE = "2023-03-01";
  public static final String INVESTIGATION_TYPE_LABEL_ONE = "INVESTIGATION_TYPE_LABEL_ONE";
  public static final String INFORMATION_SOURCE_ONE = "INFORMATION_SOURCE_ONE";
  public static final String INVESTIGATION_TYPE_ID = "INVESTIGATION_TYPE_ID";
  public static final String INVESTIGATION_TYPE_ID_ONE = "INVESTIGATION_TYPE_ID_ONE";
  public static final String INVESTIGATION_TYPE_LABEL_TWO = "INVESTIGATION_TYPE_LABEL_TWO";
  public static final String INFORMATION_SOURCE_TWO = "INFORMATION_SOURCE_TWO";
  public static final String INVESTIGATION_TYPE_LABEL_THREE = "INVESTIGATION_TYPE_LABEL_THREE";
  public static final String INFORMATION_SOURCE_THREE = "INFORMATION_SOURCE_THREE";
  public static final String INVESTIGATION_TYPE_ID_TWO = "INVESTIGATION_TYPE_ID_TWO";
  public static final String INVESTIGATION_TYPE_ID_THREE = "INVESTIGATION_TYPE_ID_THREE";
  public static final String INFORMATION_SOURCE_ID = "INFORMATION_SOURCE_ID";
  public static final String INVESTIGATION_TYPE_ID_FOUR = "INVESTIGATION_TYPE_ID_FOUR";
  private final ValueConverter medicalInvestigationValueConverter = new MedicalInvestigationValueConverter();

  @Test
  void shallReturnYearValueType() {
    assertEquals(CertificateDataValueType.MEDICAL_INVESTIGATION_LIST,
        medicalInvestigationValueConverter.getType());
  }

  @Test
  void shallReturnNotProvidedValueIfNull() {
    final var element = CertificateDataElement.builder()
        .value(
            CertificateDataValueMedicalInvestigationList.builder()
                .build()
        )
        .build();

    final var actualValue = medicalInvestigationValueConverter.convert(element);
    assertEquals(NOT_PROVIDED_VALUE, actualValue);
  }

  @Test
  void shallReturnNotProvidedValueIfAllValuesAreNull() {
    final var element = CertificateDataElement.builder()
        .config(createConfig())
        .value(
            createMedicalInvestigationValues(
                createMedicalInvestigationValue(
                    null,
                    null,
                    null)
            )
        )
        .build();

    final var actualValue = medicalInvestigationValueConverter.convert(element);
    assertEquals(NOT_PROVIDED_VALUE, actualValue);
  }

  @Test
  void shallReturnMissingValueIfDateIsNull() {
    final var element = CertificateDataElement.builder()
        .config(createConfig())
        .value(
            createMedicalInvestigationValues(
                createMedicalInvestigationValue(
                    INVESTIGATION_TYPE_ID_ONE,
                    null,
                    INFORMATION_SOURCE_ONE)
            )
        )
        .build();

    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings(INVESTIGATION_TYPE_HEADER, DATE_HEADER, INFORMATION_SOURCE_HEADER)
        )
        .values(
            createValues(
                createValue(INVESTIGATION_TYPE_LABEL_ONE, MISSING_LABEL, INFORMATION_SOURCE_ONE)
            )
        )
        .build();

    final var actualValue = medicalInvestigationValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnMissingValueIfInformationSourceTextIsNull() {
    final var element = CertificateDataElement.builder()
        .config(
            createConfig()
        )
        .value(
            createMedicalInvestigationValues(
                createMedicalInvestigationValue(
                    INVESTIGATION_TYPE_ID_ONE,
                    DATE_ONE,
                    null)
            )
        )
        .build();

    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings(INVESTIGATION_TYPE_HEADER, DATE_HEADER, INFORMATION_SOURCE_HEADER)
        )
        .values(
            createValues(
                createValue(INVESTIGATION_TYPE_LABEL_ONE, DATE_ONE, MISSING_LABEL)
            )
        )
        .build();

    final var actualValue = medicalInvestigationValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnMissingValueIfInvestigationTypeCodeIsNull() {
    final var element = CertificateDataElement.builder()
        .config(createConfig())
        .value(
            createMedicalInvestigationValues(
                createMedicalInvestigationValue(
                    null,
                    DATE_ONE,
                    INFORMATION_SOURCE_ONE)
            )
        )
        .build();

    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings(INVESTIGATION_TYPE_HEADER, DATE_HEADER, INFORMATION_SOURCE_HEADER)
        )
        .values(
            createValues(
                createValue(MISSING_LABEL, DATE_ONE, INFORMATION_SOURCE_ONE)
            )
        )
        .build();

    final var actualValue = medicalInvestigationValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnMissingValueIfDateValueIsNull() {
    final var element = CertificateDataElement.builder()
        .config(createConfig())
        .value(
            createMedicalInvestigationValues(
                CertificateDataValueMedicalInvestigation.builder()
                    .investigationType(
                        CertificateDataValueCode.builder()
                            .id(INVESTIGATION_TYPE_ID)
                            .code(INVESTIGATION_TYPE_ID_ONE)
                            .build()
                    )
                    .date(null)
                    .informationSource(
                        CertificateDataValueText.builder()
                            .id(INFORMATION_SOURCE_ID)
                            .text(INFORMATION_SOURCE_ONE)
                            .build()
                    )
                    .build()
            )
        )
        .build();

    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings(INVESTIGATION_TYPE_HEADER, DATE_HEADER, INFORMATION_SOURCE_HEADER)
        )
        .values(
            createValues(
                createValue(INVESTIGATION_TYPE_LABEL_ONE, MISSING_LABEL, INFORMATION_SOURCE_ONE)
            )
        )
        .build();

    final var actualValue = medicalInvestigationValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnMissingValueIfInformationSourceIsNull() {
    final var element = CertificateDataElement.builder()
        .config(createConfig())
        .value(
            createMedicalInvestigationValues(
                CertificateDataValueMedicalInvestigation.builder()
                    .investigationType(
                        CertificateDataValueCode.builder()
                            .id(INVESTIGATION_TYPE_ID)
                            .code(INVESTIGATION_TYPE_ID_ONE)
                            .build()
                    )
                    .date(
                        CertificateDataValueDate
                            .builder()
                            .date(LocalDate.parse(DATE_ONE))
                            .build()
                    )
                    .informationSource(null)
                    .build()
            )
        )
        .build();

    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings(INVESTIGATION_TYPE_HEADER, DATE_HEADER, INFORMATION_SOURCE_HEADER)
        )
        .values(
            createValues(
                createValue(INVESTIGATION_TYPE_LABEL_ONE, DATE_ONE, MISSING_LABEL)
            )
        )
        .build();

    final var actualValue = medicalInvestigationValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnMissingValueIfInvestigationTypeIsNull() {
    final var element = CertificateDataElement.builder()
        .config(createConfig())
        .value(
            createMedicalInvestigationValues(
                CertificateDataValueMedicalInvestigation.builder()
                    .investigationType(null)
                    .date(
                        CertificateDataValueDate
                            .builder()
                            .date(LocalDate.parse(DATE_ONE))
                            .build()
                    )
                    .informationSource(
                        CertificateDataValueText.builder()
                            .id(INFORMATION_SOURCE_ID)
                            .text(INFORMATION_SOURCE_ONE)
                            .build()
                    )
                    .build()
            )
        )
        .build();

    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings(INVESTIGATION_TYPE_HEADER, DATE_HEADER, INFORMATION_SOURCE_HEADER)
        )
        .values(
            createValues(
                createValue(MISSING_LABEL, DATE_ONE, INFORMATION_SOURCE_ONE)
            )
        )
        .build();

    final var actualValue = medicalInvestigationValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnOneInvestigation() {
    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings(INVESTIGATION_TYPE_HEADER, DATE_HEADER, INFORMATION_SOURCE_HEADER)
        )
        .values(
            createValues(
                createValue(INVESTIGATION_TYPE_LABEL_ONE, DATE_ONE, INFORMATION_SOURCE_ONE)
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createConfig()
        )
        .value(
            createMedicalInvestigationValues(
                createMedicalInvestigationValue(INVESTIGATION_TYPE_ID_ONE, DATE_ONE,
                    INFORMATION_SOURCE_ONE)
            )
        )
        .build();

    final var actualValue = medicalInvestigationValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnInvestigationWithCodeIdIfLabelMissing() {
    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings(INVESTIGATION_TYPE_HEADER, DATE_HEADER, INFORMATION_SOURCE_HEADER)
        )
        .values(
            createValues(
                createValue(INVESTIGATION_TYPE_ID_FOUR, DATE_ONE, INFORMATION_SOURCE_ONE)
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createConfig()
        )
        .value(
            createMedicalInvestigationValues(
                createMedicalInvestigationValue(INVESTIGATION_TYPE_ID_FOUR, DATE_ONE,
                    INFORMATION_SOURCE_ONE)
            )
        )
        .build();

    final var actualValue = medicalInvestigationValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnManyInvestigations() {
    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings(INVESTIGATION_TYPE_HEADER, DATE_HEADER, INFORMATION_SOURCE_HEADER)
        )
        .values(
            createValues(
                createValue(INVESTIGATION_TYPE_LABEL_ONE, DATE_ONE, INFORMATION_SOURCE_ONE),
                createValue(INVESTIGATION_TYPE_LABEL_TWO, DATE_TWO, INFORMATION_SOURCE_TWO),
                createValue(INVESTIGATION_TYPE_LABEL_THREE, DATE_THREE,
                    INFORMATION_SOURCE_THREE)
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createConfig()
        )
        .value(
            createMedicalInvestigationValues(
                createMedicalInvestigationValue(INVESTIGATION_TYPE_ID_ONE, DATE_ONE,
                    INFORMATION_SOURCE_ONE),
                createMedicalInvestigationValue(INVESTIGATION_TYPE_ID_TWO, DATE_TWO,
                    INFORMATION_SOURCE_TWO),
                createMedicalInvestigationValue(INVESTIGATION_TYPE_ID_THREE, DATE_THREE,
                    INFORMATION_SOURCE_THREE)
            )
        )
        .build();

    final var actualValue = medicalInvestigationValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  private static CertificateDataValueMedicalInvestigationList createMedicalInvestigationValues(
      CertificateDataValueMedicalInvestigation... values) {
    return CertificateDataValueMedicalInvestigationList.builder()
        .list(
            List.of(values)
        )
        .build();
  }

  private static CertificateDataValueMedicalInvestigation createMedicalInvestigationValue(
      String INVESTIGATION_TYPE_ID_ONE, String dateOne, String INFORMATION_SOURCE_ONE) {
    return CertificateDataValueMedicalInvestigation.builder()
        .investigationType(
            CertificateDataValueCode.builder()
                .id(INVESTIGATION_TYPE_ID)
                .code(INVESTIGATION_TYPE_ID_ONE)
                .build()
        )
        .date(
            CertificateDataValueDate.builder()
                .id(DATE_ID)
                .date(dateOne != null ? LocalDate.parse(dateOne) : null)
                .build()
        )
        .informationSource(
            CertificateDataValueText.builder()
                .id(INFORMATION_SOURCE_ID)
                .text(INFORMATION_SOURCE_ONE)
                .build()
        )
        .build();
  }

  private static CertificateDataConfigMedicalInvestigation createConfig() {
    return CertificateDataConfigMedicalInvestigation.builder()
        .typeText(INVESTIGATION_TYPE_HEADER)
        .dateText(DATE_HEADER)
        .informationSourceText(INFORMATION_SOURCE_HEADER)
        .list(
            List.of(
                MedicalInvestigation.builder()
                    .typeOptions(
                        List.of(
                            CodeItem.builder()
                                .id(INVESTIGATION_TYPE_ID)
                                .code(INVESTIGATION_TYPE_ID_ONE)
                                .label(INVESTIGATION_TYPE_LABEL_ONE)
                                .build(),
                            CodeItem.builder()
                                .id(INVESTIGATION_TYPE_ID)
                                .code(INVESTIGATION_TYPE_ID_TWO)
                                .label(INVESTIGATION_TYPE_LABEL_TWO)
                                .build(),
                            CodeItem.builder()
                                .id(INVESTIGATION_TYPE_ID)
                                .code(INVESTIGATION_TYPE_ID_THREE)
                                .label(INVESTIGATION_TYPE_LABEL_THREE)
                                .build()
                        )
                    )
                    .build()
            )
        )
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