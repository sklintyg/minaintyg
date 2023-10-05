package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueItemList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificationQuestionValueItem;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateList;

class DateListValueConverterTest {

  private static final String ID_ONE = "ID_ONE";
  private static final String LABEL_ONE = "LABEL_ONE";
  private static final LocalDate VALUE_ONE = LocalDate.now();
  private static final String ID_TWO = "ID_TWO";
  private static final String LABEL_TWO = "LABEL_TWO";
  private static final String NOT_PROVIDED = "Ej angivet";

  private static final LocalDate VALUE_TWO = LocalDate.now().minusDays(1);
  private ValueConverter dateListValueConverter = new DateListValueConverter();

  @Test
  void shallReturnOneListItemIfOneDateValue() {
    final var expectedValue = createValueItemList(
        createValueItem(LABEL_ONE, VALUE_ONE.toString())
    );

    final var element = CertificateDataElement.builder()
        .config(
            createCheckboxMultipleDateConfig(
                createCheckboxMultipleDate(ID_ONE, LABEL_ONE)
            )
        )
        .value(
            createDateList(
                createValueDate(ID_ONE, VALUE_ONE)
            )
        )
        .build();

    final var actualValue = dateListValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnOneListItemWithoutValueIfNoDateValue() {
    final var expectedValue = createValueItemList(
        createValueItem(LABEL_ONE, NOT_PROVIDED)
    );

    final var element = CertificateDataElement.builder()
        .config(
            createCheckboxMultipleDateConfig(
                createCheckboxMultipleDate(ID_ONE, LABEL_ONE)
            )
        )
        .value(
            createDateList()
        )
        .build();

    final var actualValue = dateListValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnOneListItemWithoutValueIfNullDateValue() {
    final var expectedValue = createValueItemList(
        createValueItem(LABEL_ONE, NOT_PROVIDED)
    );

    final var element = CertificateDataElement.builder()
        .config(
            createCheckboxMultipleDateConfig(
                createCheckboxMultipleDate(ID_ONE, LABEL_ONE)
            )
        )
        .value(
            CertificateDataValueDateList.builder()
                .list(null)
                .build()
        )
        .build();

    final var actualValue = dateListValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnTwoListItemWithoutValueIfNoDateValue() {
    final var expectedValue = createValueItemList(
        createValueItem(LABEL_ONE, NOT_PROVIDED),
        createValueItem(LABEL_TWO, NOT_PROVIDED)
    );

    final var element = CertificateDataElement.builder()
        .config(
            createCheckboxMultipleDateConfig(
                createCheckboxMultipleDate(ID_ONE, LABEL_ONE),
                createCheckboxMultipleDate(ID_TWO, LABEL_TWO)
            )
        )
        .value(
            createDateList()
        )
        .build();

    final var actualValue = dateListValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnTwoListItemIfTwoDateValue() {
    final var expectedValue = createValueItemList(
        createValueItem(LABEL_ONE, VALUE_ONE.toString()),
        createValueItem(LABEL_TWO, VALUE_TWO.toString())
    );

    final var element = CertificateDataElement.builder()
        .config(
            createCheckboxMultipleDateConfig(
                createCheckboxMultipleDate(ID_ONE, LABEL_ONE),
                createCheckboxMultipleDate(ID_TWO, LABEL_TWO)
            )
        )
        .value(
            createDateList(
                createValueDate(ID_ONE, VALUE_ONE),
                createValueDate(ID_TWO, VALUE_TWO)
            )
        )
        .build();

    final var actualValue = dateListValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnTwoListItemOneWithoutValueIfOneDateValue() {
    final var expectedValue = createValueItemList(
        createValueItem(LABEL_ONE, VALUE_ONE.toString()),
        createValueItem(LABEL_TWO, NOT_PROVIDED)
    );

    final var element = CertificateDataElement.builder()
        .config(
            createCheckboxMultipleDateConfig(
                createCheckboxMultipleDate(ID_ONE, LABEL_ONE),
                createCheckboxMultipleDate(ID_TWO, LABEL_TWO)
            )
        )
        .value(
            createDateList(
                createValueDate(ID_ONE, VALUE_ONE)
            )
        )
        .build();

    final var actualValue = dateListValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  private CertificateQuestionValueItemList createValueItemList(
      CertificationQuestionValueItem... valueItems) {
    return CertificateQuestionValueItemList.builder()
        .values(
            List.of(
                valueItems
            )
        )
        .build();
  }

  private static CertificationQuestionValueItem createValueItem(String label, String value) {
    return CertificationQuestionValueItem.builder()
        .label(label)
        .value(value)
        .build();
  }

  private CertificateDataConfigCheckboxMultipleDate createCheckboxMultipleDateConfig(
      CheckboxMultipleDate... checkboxMultipleDates) {
    return CertificateDataConfigCheckboxMultipleDate.builder()
        .list(
            List.of(
                checkboxMultipleDates
            )
        )
        .build();
  }

  private CheckboxMultipleDate createCheckboxMultipleDate(String id, String label) {
    return CheckboxMultipleDate.builder()
        .id(id)
        .label(label)
        .build();
  }

  private CertificateDataValueDateList createDateList(CertificateDataValueDate... valueDate) {
    return CertificateDataValueDateList.builder()
        .list(
            List.of(
                valueDate
            )
        )
        .build();
  }

  private CertificateDataValueDate createValueDate(String id, LocalDate date) {
    return CertificateDataValueDate.builder()
        .id(id)
        .date(date)
        .build();
  }
}