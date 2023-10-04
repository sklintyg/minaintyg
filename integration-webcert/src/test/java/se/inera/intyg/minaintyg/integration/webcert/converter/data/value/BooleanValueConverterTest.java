package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueBoolean;

class BooleanValueConverterTest {

  private static final String NOT_PROVIDED = "Ej angivet";
  private static final String TRUE_BOOLEAN = "Ja";
  private static final String FALSE_BOOLEAN = "Nej";

  private ValueConverter booleanValueConverter = new BooleanValueConverter();

  @Test
  void shouldConvertCertificateDataValueRadioBooleanWithValueTrue() {
    final var elements = createElement(CertificateDataConfigRadioBoolean.builder().build(),
        CertificateDataValueBoolean.builder()
            .selected(true)
            .build()
    );

    final var expectedResult = CertificateQuestionValueText.builder()
        .value(TRUE_BOOLEAN)
        .build();

    final var result = booleanValueConverter.convert(elements);
    assertEquals(expectedResult, result);
  }

  @Test
  void shouldConvertCertificateDataValueRadioBooleanWithValueFalse() {
    final var elements = createElement(CertificateDataConfigRadioBoolean.builder().build(),
        CertificateDataValueBoolean.builder()
            .selected(false)
            .build());

    final var expectedResult = CertificateQuestionValueText.builder()
        .value(FALSE_BOOLEAN)
        .build();

    final var result = booleanValueConverter.convert(elements);
    assertEquals(expectedResult, result);
  }

  @Test
  void shouldConvertCertificateDataValueRadioBooleanWithNoValue() {
    final var elements = createElement(CertificateDataConfigRadioBoolean.builder().build(),
        CertificateDataValueBoolean.builder()
            .build());

    final var expectedResult = CertificateQuestionValueText.builder()
        .value(NOT_PROVIDED)
        .build();

    final var result = booleanValueConverter.convert(elements);
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