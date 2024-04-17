package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter.NOT_PROVIDED_VALUE;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

class DateValueConverterTest {

  private final ValueConverter dateValueConverter = new DateValueConverter();

  @Test
  void shallReturnDateValueType() {
    assertEquals(CertificateDataValueType.DATE, dateValueConverter.getType());
  }

  @Test
  void shallReturnNotProvidedValueIfNull() {
    final var element = CertificateDataElement.builder()
        .value(
            CertificateDataValueDate.builder()
                .build()
        )
        .build();

    final var actualValue = dateValueConverter.convert(element);
    assertEquals(NOT_PROVIDED_VALUE, actualValue);
  }

  @Test
  void shallReturnTextValueWithDateIfDateExists() {
    final var expectedValue = CertificateQuestionValueText.builder()
        .value("2023-01-01")
        .build();

    final var element = CertificateDataElement.builder()
        .value(
            CertificateDataValueDate.builder()
                .date(LocalDate.parse(expectedValue.getValue()))
                .build()
        )
        .build();

    final var actualValue = dateValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallThrowIllegalArgumentIfWrongType() {
    final var element = CertificateDataElement.builder()
        .value(
            CertificateDataValueText.builder().build()
        )
        .build();

    assertThrows(IllegalArgumentException.class, () -> dateValueConverter.convert(element));
  }
}