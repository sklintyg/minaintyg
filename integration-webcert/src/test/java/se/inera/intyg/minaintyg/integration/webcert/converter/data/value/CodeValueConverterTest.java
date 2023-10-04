package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioMultipleCodeOptionalDropdown;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.RadioMultipleCodeOptionalDropdown;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCode;

class CodeValueConverterTest {

  private static final String CONFIG_ID = "configId";
  private static final String CODE = "NUVARANDE_ARBETE";
  private static final String LABEL_VALUE = "labelValue";
  private static final String NOT_PROVIDED = "Ej angivet";

  private ValueConverter codeValueConverter = new CodeValueConverter();

  @Test
  void shouldConvertCertificateCodeValue() {
    final var value = CertificateDataValueCode.builder()
        .code(CONFIG_ID)
        .build();

    final var config = CertificateDataConfigRadioMultipleCodeOptionalDropdown.builder()
        .list(
            List.of(
                RadioMultipleCodeOptionalDropdown.builder()
                    .id(CONFIG_ID)
                    .label(LABEL_VALUE)
                    .build()
            )
        ).build();
    final var element = createElement(config, value);
    final var expectedValue = CertificateQuestionValueText.builder()
        .value(LABEL_VALUE)
        .build();
    final var result = codeValueConverter.convert(element);
    assertEquals(expectedValue, result);
  }

  @Test
  void shouldConvertCertificateCodeNoValue() {
    final var value = CertificateDataValueCode.builder()
        .code(CODE)
        .build();

    final var config = CertificateDataConfigRadioMultipleCodeOptionalDropdown.builder()
        .list(
            List.of(
                RadioMultipleCodeOptionalDropdown.builder()
                    .id(CONFIG_ID)
                    .label(LABEL_VALUE)
                    .build()
            )
        ).build();
    final var element = createElement(config, value);
    final var expectedValue = CertificateQuestionValueText.builder()
        .value(NOT_PROVIDED)
        .build();
    final var result = codeValueConverter.convert(element);
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