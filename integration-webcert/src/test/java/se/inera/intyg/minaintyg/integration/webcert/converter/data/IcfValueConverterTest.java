package se.inera.intyg.minaintyg.integration.webcert.converter.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTextArea;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataIcfValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;

class IcfValueConverterTest {

  private static final String TEXT_VALUE = "textValue";
  private static final String NOT_PROVIDED = "Ej angivet";
  private ValueConverter icfValueConverter = new IcfValueConverter();

  @Test
  void shouldConvertCertificateIcfValue() {
    final var value = CertificateDataIcfValue.builder()
        .text(TEXT_VALUE)
        .build();
    final var config = CertificateDataConfigTextArea.builder().build();
    final var element = createElement(config, value);
    final var expectedValue = CertificateQuestionValueText.builder()
        .value(TEXT_VALUE)
        .build();
    final var result = icfValueConverter.convert(element);
    assertEquals(expectedValue, result);
  }

  @Test
  void shouldConvertCertificateIcfNoValue() {
    final var value = CertificateDataIcfValue.builder().build();
    final var config = CertificateDataConfigTextArea.builder().build();
    final var element = createElement(config, value);
    final var expectedValue = CertificateQuestionValueText.builder()
        .value(NOT_PROVIDED)
        .build();
    final var result = icfValueConverter.convert(element);
    assertEquals(expectedValue, result);
  }

  private static CertificateDataElement createElement(CertificateDataConfig config,
      CertificateDataValue value) {
    return CertificateDataElement.builder()
        .config(config)
        .value(value)
        .build();
  }
}