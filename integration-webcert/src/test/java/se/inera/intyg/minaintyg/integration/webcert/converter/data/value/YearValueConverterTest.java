package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter.NOT_PROVIDED_VALUE;

import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataTextValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueYear;

class YearValueConverterTest {

  private final ValueConverter yearValueConverter = new YearValueConverter();

  @Test
  void shallReturnYearValueType() {
    assertEquals(CertificateDataValueType.YEAR, yearValueConverter.getType());
  }

  @Test
  void shallReturnNotProvidedValueIfNull() {
    final var element = CertificateDataElement.builder()
        .value(
            CertificateDataValueYear.builder()
                .build()
        )
        .build();

    final var actualValue = yearValueConverter.convert(element);
    assertEquals(NOT_PROVIDED_VALUE, actualValue);
  }

  @Test
  void shallReturnTextValueWithYearIfYearExists() {
    final var expectedValue = CertificateQuestionValueText.builder()
        .value("2023")
        .build();

    final var element = CertificateDataElement.builder()
        .value(
            CertificateDataValueYear.builder()
                .year(Integer.valueOf(expectedValue.getValue()))
                .build()
        )
        .build();

    final var actualValue = yearValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallThrowIllegalArgumentIfWrongType() {
    final var element = CertificateDataElement.builder()
        .value(
            CertificateDataTextValue.builder().build()
        )
        .build();

    assertThrows(IllegalArgumentException.class, () -> yearValueConverter.convert(element));
  }
}