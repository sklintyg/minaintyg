package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTextArea;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueViewList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueViewText;

class ViewListValueConverterTest {

  private static final String TEXT_VALUE = "textValue";
  private static final String NOT_PROVIDED = "Ej angivet";

  private ValueConverter valueConverter = new ViewListValueConverter();

  @Test
  void shouldConvertValue() {
    final var elements = createElement(
        CertificateDataConfigTextArea.builder().build(),
        CertificateDataValueViewList.builder()
            .list(
                List.of(
                    CertificateDataValueViewText.builder()
                        .text(TEXT_VALUE)
                        .build(),
                    CertificateDataValueViewText.builder()
                        .text(TEXT_VALUE)
                        .build()
                )
            )
            .build()
    );

    final var expectedResult = CertificateQuestionValueList.builder()
        .values(List.of(TEXT_VALUE, TEXT_VALUE))
        .build();

    final var result = valueConverter.convert(elements);
    assertEquals(expectedResult, result);
  }

  @Test
  void shouldConvertWhenNoValue() {
    final var elements = createElement(
        CertificateDataConfigTextArea.builder().build(),
        CertificateDataValueViewList.builder().build()
    );

    final var expectedResult = CertificateQuestionValueText.builder()
        .value(NOT_PROVIDED)
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