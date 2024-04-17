package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTextArea;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueText;

class TextValueConverterTest {

  private static final String TEXT_VALUE = "textValue";
  private static final String NOT_PROVIDED = "Ej angivet";

  private ValueConverter valueConverter = new TextValueConverter();

  @Test
  void shouldConvertCertificateDataTextValue() {
    final var elements = createElement(
        CertificateDataConfigTextArea.builder().build(),
        CertificateDataValueText.builder()
            .text(TEXT_VALUE)
            .build()
    );

    final var expectedResult = CertificateQuestionValueText.builder()
        .value(TEXT_VALUE)
        .build();

    final var result = valueConverter.convert(elements);
    assertEquals(expectedResult, result);
  }

  @Test
  void shouldConvertCertificateDataTextValueWithNoValue() {
    final var elements = createElement(
        CertificateDataConfigTextArea.builder().build(),
        CertificateDataValueText.builder()
            .build()
    );

    final var expectedResult = CertificateQuestionValueText.builder()
        .value(NOT_PROVIDED)
        .build();

    final var result = valueConverter.convert(elements);
    assertEquals(expectedResult, result);
  }

  private static CertificateDataElement createElement(CertificateDataConfig config,
      CertificateDataValue value) {
    return CertificateDataElement.builder()
        .config(config)
        .value(value)
        .build();
  }
}