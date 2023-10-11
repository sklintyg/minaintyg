package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter.NOT_PROVIDED_VALUE;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigSickLeavePeriod;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CheckboxDateRange;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateRange;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateRangeList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

class DateRangeListValueConverterTest {

  public static final String HEADING_ONE = "Nedsättningsgrad";
  public static final String HEADING_TWO = "Från och med";
  public static final String HEADING_THREE = "Till och med";
  public static final String ID_ONE = "ID_ONE";
  public static final String LABEL_ONE = "LABEL_ONE";
  public static final String VALUE_FROM_ONE = "2023-01-01";
  public static final String VALUE_TO_ONE = "2023-01-20";
  public static final String ID_TWO = "ID_TWO";
  public static final String LABEL_TWO = "LABEL_TWO";
  public static final String VALUE_FROM_TWO = "2023-01-21";
  public static final String VALUE_TO_TWO = "2023-01-30";
  private final ValueConverter dateRangeListValueConverter = new DateRangeListValueConverter();

  @Test
  void shallReturnDateRangeListValueType() {
    assertEquals(CertificateDataValueType.DATE_RANGE_LIST, dateRangeListValueConverter.getType());
  }

  @Test
  void shallReturnNotProvidedValueIfNull() {
    final var element = CertificateDataElement.builder()
        .value(
            CertificateDataValueDateRangeList.builder()
                .build()
        )
        .build();

    final var actualValue = dateRangeListValueConverter.convert(element);
    assertEquals(NOT_PROVIDED_VALUE, actualValue);
  }

  @Test
  void shallReturnNotProvidedValueIfRangeListIsEmpty() {
    final var element = CertificateDataElement.builder()
        .value(
            CertificateDataValueDateRangeList.builder()
                .list(Collections.emptyList())
                .build()
        )
        .build();

    final var actualValue = dateRangeListValueConverter.convert(element);
    assertEquals(NOT_PROVIDED_VALUE, actualValue);
  }

  @Test
  void shallReturnDateRangeAsTableWithOneValue() {
    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings(HEADING_ONE, HEADING_TWO, HEADING_THREE)
        )
        .values(
            createValues(
                createValue(LABEL_ONE, VALUE_FROM_ONE, VALUE_TO_ONE)
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createSickLeaveConfig(
                createCheckboxDateRange(ID_ONE, LABEL_ONE)
            )
        )
        .value(
            createDateRangeList(
                createDateRange(ID_ONE, VALUE_FROM_ONE, VALUE_TO_ONE)
            )
        )
        .build();

    final var actualValue = dateRangeListValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnDateRangeAsTableWithMultipleValues() {
    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings(HEADING_ONE, HEADING_TWO, HEADING_THREE)
        )
        .values(
            createValues(
                createValue(LABEL_ONE, VALUE_FROM_ONE, VALUE_TO_ONE),
                createValue(LABEL_TWO, VALUE_FROM_TWO, VALUE_TO_TWO)
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createSickLeaveConfig(
                createCheckboxDateRange(ID_ONE, LABEL_ONE),
                createCheckboxDateRange(ID_TWO, LABEL_TWO)
            )
        )
        .value(
            createDateRangeList(
                createDateRange(ID_ONE, VALUE_FROM_ONE, VALUE_TO_ONE),
                createDateRange(ID_TWO, VALUE_FROM_TWO, VALUE_TO_TWO)
            )
        )
        .build();

    final var actualValue = (CertificateQuestionValueTable) dateRangeListValueConverter.convert(
        element);
    assertTrue(actualValue.getValues().contains(expectedValue.getValues().get(0)),
        "Missing value: '%s'".formatted(expectedValue.getValues().get(0))
    );
    assertTrue(actualValue.getValues().contains(expectedValue.getValues().get(1)),
        "Missing value: '%s'".formatted(expectedValue.getValues().get(1))
    );
  }

  @Test
  void shallReturnDateRangeAsTableWithCorrectOrderWithMultipleValues() {
    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings(HEADING_ONE, HEADING_TWO, HEADING_THREE)
        )
        .values(
            createValues(
                createValue(LABEL_TWO, VALUE_FROM_TWO, VALUE_TO_TWO),
                createValue(LABEL_ONE, VALUE_FROM_ONE, VALUE_TO_ONE)
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createSickLeaveConfig(
                createCheckboxDateRange(ID_ONE, LABEL_ONE),
                createCheckboxDateRange(ID_TWO, LABEL_TWO)
            )
        )
        .value(
            createDateRangeList(
                createDateRange(ID_ONE, VALUE_FROM_ONE, VALUE_TO_ONE),
                createDateRange(ID_TWO, VALUE_FROM_TWO, VALUE_TO_TWO)
            )
        )
        .build();

    final var actualValue = dateRangeListValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnDateRangeAsTableIfRangeListIsContainsPartlyValues() {
    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings(HEADING_ONE, HEADING_TWO, HEADING_THREE)
        )
        .values(
            createValues(
                createValue(LABEL_TWO, VALUE_FROM_TWO, VALUE_TO_TWO)
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createSickLeaveConfig(
                createCheckboxDateRange(ID_ONE, LABEL_ONE),
                createCheckboxDateRange(ID_TWO, LABEL_TWO)
            )
        )
        .value(
            createDateRangeList(
                createDateRange(ID_TWO, VALUE_FROM_TWO, VALUE_TO_TWO)
            )
        )
        .build();

    final var actualValue = dateRangeListValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnDateRangeIDAsLabelIfMissingConfig() {
    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings(HEADING_ONE, HEADING_TWO, HEADING_THREE)
        )
        .values(
            createValues(
                createValue(ID_TWO, VALUE_FROM_TWO, VALUE_TO_TWO)
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .value(
            createDateRangeList(
                createDateRange(ID_TWO, VALUE_FROM_TWO, VALUE_TO_TWO)
            )
        )
        .build();

    final var actualValue = dateRangeListValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
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

  private static CertificateDataConfigSickLeavePeriod createSickLeaveConfig(
      CheckboxDateRange... config) {
    return CertificateDataConfigSickLeavePeriod.builder()
        .list(
            List.of(
                config
            )
        )
        .build();
  }

  private static CheckboxDateRange createCheckboxDateRange(String id, String label) {
    return CheckboxDateRange.builder()
        .id(id)
        .label(label)
        .build();
  }

  private static CertificateDataValueDateRangeList createDateRangeList(
      CertificateDataValueDateRange... dateRanges) {
    return CertificateDataValueDateRangeList.builder()
        .list(
            List.of(
                dateRanges
            )
        )
        .build();
  }

  private static CertificateDataValueDateRange createDateRange(String idTwo, String valueFromTwo,
      String valueToTwo) {
    return CertificateDataValueDateRange.builder()
        .id(idTwo)
        .from(LocalDate.parse(valueFromTwo))
        .to(LocalDate.parse(valueToTwo))
        .build();
  }
}