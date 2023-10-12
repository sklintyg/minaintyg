package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter.NOT_PROVIDED_VALUE;

import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataTextValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDouble;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

class DoubleValueConverterTest {

  private final ValueConverter doubleValueConverter = new DoubleValueConverter();

  @Test
  void shallReturnDoubleValueType() {
    assertEquals(CertificateDataValueType.DOUBLE, doubleValueConverter.getType());
  }

  @Test
  void shallReturnNotProvidedValueIfNull() {
    final var element = CertificateDataElement.builder()
        .value(
            CertificateDataValueDouble.builder()
                .build()
        )
        .build();

    final var actualValue = doubleValueConverter.convert(element);
    assertEquals(NOT_PROVIDED_VALUE, actualValue);
  }

  @Test
  void shallReturnTextValueWithDoubleIfDoubleExists() {
    final var expectedValue = CertificateQuestionValueText.builder()
        .value("1,0")
        .build();

    final var element = CertificateDataElement.builder()
        .value(
            CertificateDataValueDouble.builder()
                .value(Double.valueOf(expectedValue.getValue().replace(",", ".")))
                .build()
        )
        .build();

    final var actualValue = doubleValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallThrowIllegalArgumentIfWrongType() {
    final var element = CertificateDataElement.builder()
        .value(
            CertificateDataTextValue.builder().build()
        )
        .build();

    assertThrows(IllegalArgumentException.class, () -> doubleValueConverter.convert(element));
  }
}