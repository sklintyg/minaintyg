package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter.MISSING_LABEL;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTextArea;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigViewTable;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.ViewColumn;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueViewRow;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueViewTable;

class ViewTableValueConverterTest {

  private static final String VALUE_1 = "value1";
  private static final String VALUE_2 = "value2";
  private static final String VALUE_3 = "value3";
  private static final String HEADING_1 = "heading1";
  private static final String HEADING_2 = "heading2";
  private static final String HEADING_3 = "heading3";
  private static final String NOT_PROVIDED = "Ej angivet";

  private ValueConverter valueConverter = new ViewTableValueConverter();

  @Test
  void shouldConvertValue() {
    final var elements = createElement(
        CertificateDataConfigViewTable.builder()
            .columns(
                List.of(
                    ViewColumn.builder().text(HEADING_1).build(),
                    ViewColumn.builder().text(HEADING_2).build(),
                    ViewColumn.builder().text(HEADING_3).build()
                )
            )
            .build(),
        CertificateDataValueViewTable.builder()
            .rows(List.of(
                CertificateDataValueViewRow
                    .builder()
                    .columns(List.of(
                        CertificateDataValueText
                            .builder()
                            .text(VALUE_1)
                            .build(),
                        CertificateDataValueText
                            .builder()
                            .text(VALUE_2)
                            .build(),
                        CertificateDataValueText
                            .builder()
                            .text(VALUE_3)
                            .build()
                    ))
                    .build()
            ))
            .build()
    );

    final var expectedResult = CertificateQuestionValueTable.builder()
        .headings(List.of(HEADING_1, HEADING_2, HEADING_3))
        .values(
            List.of(
                List.of(VALUE_1, VALUE_2, VALUE_3)
            )
        )
        .build();

    final var result = valueConverter.convert(elements);
    assertEquals(expectedResult, result);
  }

  @Test
  void shouldConvertValueIncludingEmptyHeading() {
    final var elements = createElement(
        CertificateDataConfigViewTable.builder()
            .columns(
                List.of(
                    ViewColumn.builder().build(),
                    ViewColumn.builder().text(HEADING_2).build(),
                    ViewColumn.builder().text(HEADING_3).build()
                )
            )
            .build(),
        CertificateDataValueViewTable.builder()
            .rows(List.of(
                CertificateDataValueViewRow
                    .builder()
                    .columns(List.of(
                        CertificateDataValueText
                            .builder()
                            .text(VALUE_1)
                            .build(),
                        CertificateDataValueText
                            .builder()
                            .text(VALUE_2)
                            .build(),
                        CertificateDataValueText
                            .builder()
                            .text(VALUE_3)
                            .build()
                    ))
                    .build()
            ))
            .build()
    );

    final var expectedResult = CertificateQuestionValueTable.builder()
        .headings(List.of("", HEADING_2, HEADING_3))
        .values(
            List.of(
                List.of(VALUE_1, VALUE_2, VALUE_3)
            )
        )
        .build();

    final var result = valueConverter.convert(elements);
    assertEquals(expectedResult, result);
  }

  @Test
  void shouldConvertWhenNoValue() {
    final var elements = createElement(
        CertificateDataConfigViewTable.builder().build(),
        CertificateDataValueViewTable.builder().build()
    );

    final var expectedResult = CertificateQuestionValueText.builder()
        .value(NOT_PROVIDED)
        .build();

    final var result = valueConverter.convert(elements);
    assertEquals(expectedResult, result);
  }

  @Test
  void shouldConvertWhenWrongConfig() {
    final var elements = createElement(
        CertificateDataConfigTextArea.builder().build(),
        CertificateDataValueViewTable.builder()
            .rows(List.of(
                CertificateDataValueViewRow
                    .builder()
                    .columns(List.of(
                        CertificateDataValueText
                            .builder()
                            .text(VALUE_1)
                            .build(),
                        CertificateDataValueText
                            .builder()
                            .text(VALUE_2)
                            .build(),
                        CertificateDataValueText
                            .builder()
                            .text(VALUE_3)
                            .build()
                    ))
                    .build()
            ))
            .build()
    );

    final var expectedResult = CertificateQuestionValueTable.builder()
        .headings(List.of(MISSING_LABEL))
        .values(
            List.of(
                List.of(VALUE_1, VALUE_2, VALUE_3)
            )
        )
        .build();

    final var result = valueConverter.convert(elements);
    assertEquals(expectedResult, result);
  }

  private CertificateDataElement createElement(CertificateDataConfig config,
      CertificateDataValue value) {
    return CertificateDataElement.builder()
        .config(config)
        .value(value)
        .build();
  }
}