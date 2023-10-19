package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter.NOT_PROVIDED_VALUE;

import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataTextValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueInteger;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

class IntegerValueConverterTest {

  private final ValueConverter integerValueConverter = new IntegerValueConverter();
  public static final String UNIT_OF_MEASUREMENT = "%";


  @Test
  void shallReturnIntegerValueType() {
    assertEquals(CertificateDataValueType.INTEGER, integerValueConverter.getType());
  }

  @Test
  void shallReturnNotProvidedValueIfNull() {
    final var element = CertificateDataElement.builder()
        .value(
            CertificateDataValueInteger.builder()
                .build()
        )
        .build();

    final var actualValue = integerValueConverter.convert(element);
    assertEquals(NOT_PROVIDED_VALUE, actualValue);
  }

  @Test
  void shallReturnTextValueWithIntegerIfIntegerExists() {
    final var expectedValue = CertificateQuestionValueText.builder()
        .value("10")
        .build();

    final var element = CertificateDataElement.builder()
        .value(
            CertificateDataValueInteger.builder()
                .value(Integer.valueOf(expectedValue.getValue()))
                .build()
        )
        .build();

    final var actualValue = integerValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallThrowIllegalArgumentIfWrongType() {
    final var element = CertificateDataElement.builder()
        .value(
            CertificateDataTextValue.builder().build()
        )
        .build();

    assertThrows(IllegalArgumentException.class, () -> integerValueConverter.convert(element));
  }

  @Test
  void shallReturnTextValueWithIntegerAndUnitOfMeasurementIfBothExists() {
    final var expectedValue = CertificateQuestionValueText.builder()
        .value("10%")
        .build();

    final var element = CertificateDataElement.builder()
        .value(
            CertificateDataValueInteger.builder()
                .value(10)
                .unitOfMeasurement(UNIT_OF_MEASUREMENT)
                .build()
        ).build();

    final var actualValue = integerValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }
}