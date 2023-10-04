package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collections;
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
    final var expectedValue = CertificateQuestionValueItemList.builder()
        .values(
            List.of(
                CertificationQuestionValueItem.builder()
                    .label(LABEL_ONE)
                    .value(VALUE_ONE.toString())
                    .build()
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            CertificateDataConfigCheckboxMultipleDate.builder()
                .list(
                    List.of(
                        CheckboxMultipleDate.builder()
                            .id(ID_ONE)
                            .label(LABEL_ONE)
                            .build()
                    )
                )
                .build()
        )
        .value(
            CertificateDataValueDateList.builder()
                .list(
                    List.of(
                        CertificateDataValueDate.builder()
                            .id(ID_ONE)
                            .date(VALUE_ONE)
                            .build()
                    )
                )
                .build()
        )
        .build();

    final var actualValue = dateListValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnOneListItemWithoutValueIfNoDateValue() {
    final var expectedValue = CertificateQuestionValueItemList.builder()
        .values(
            List.of(
                CertificationQuestionValueItem.builder()
                    .label(LABEL_ONE)
                    .value(NOT_PROVIDED)
                    .build()
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            CertificateDataConfigCheckboxMultipleDate.builder()
                .list(
                    List.of(
                        CheckboxMultipleDate.builder()
                            .id(ID_ONE)
                            .label(LABEL_ONE)
                            .build()
                    )
                )
                .build()
        )
        .value(
            CertificateDataValueDateList.builder()
                .list(
                    Collections.emptyList()
                )
                .build()
        )
        .build();

    final var actualValue = dateListValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnTwoListItemIfTwoDateValue() {
    final var expectedValue = CertificateQuestionValueItemList.builder()
        .values(
            List.of(
                CertificationQuestionValueItem.builder()
                    .label(LABEL_ONE)
                    .value(VALUE_ONE.toString())
                    .build(),
                CertificationQuestionValueItem.builder()
                    .label(LABEL_TWO)
                    .value(VALUE_TWO.toString())
                    .build()
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            CertificateDataConfigCheckboxMultipleDate.builder()
                .list(
                    List.of(
                        CheckboxMultipleDate.builder()
                            .id(ID_ONE)
                            .label(LABEL_ONE)
                            .build(),
                        CheckboxMultipleDate.builder()
                            .id(ID_TWO)
                            .label(LABEL_TWO)
                            .build()
                    )
                )
                .build()
        )
        .value(
            CertificateDataValueDateList.builder()
                .list(
                    List.of(
                        CertificateDataValueDate.builder()
                            .id(ID_ONE)
                            .date(VALUE_ONE)
                            .build(),
                        CertificateDataValueDate.builder()
                            .id(ID_TWO)
                            .date(VALUE_TWO)
                            .build()
                    )
                )
                .build()
        )
        .build();

    final var actualValue = dateListValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnTwoListItemOneWithoutValueIfOneDateValue() {
    final var expectedValue = CertificateQuestionValueItemList.builder()
        .values(
            List.of(
                CertificationQuestionValueItem.builder()
                    .label(LABEL_ONE)
                    .value(VALUE_ONE.toString())
                    .build(),
                CertificationQuestionValueItem.builder()
                    .label(LABEL_TWO)
                    .value(NOT_PROVIDED)
                    .build()
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            CertificateDataConfigCheckboxMultipleDate.builder()
                .list(
                    List.of(
                        CheckboxMultipleDate.builder()
                            .id(ID_ONE)
                            .label(LABEL_ONE)
                            .build(),
                        CheckboxMultipleDate.builder()
                            .id(ID_TWO)
                            .label(LABEL_TWO)
                            .build()
                    )
                )
                .build()
        )
        .value(
            CertificateDataValueDateList.builder()
                .list(
                    List.of(
                        CertificateDataValueDate.builder()
                            .id(ID_ONE)
                            .date(VALUE_ONE)
                            .build()
                    )
                )
                .build()
        )
        .build();

    final var actualValue = dateListValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }
}