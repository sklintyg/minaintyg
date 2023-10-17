package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTextArea;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CheckboxMultipleCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCodeList;

class CodeListValueConverterTest {

  private static final String CODE_ONE = "NUVARANDE_ARBETE";
  private static final String CODE_ONE_MATCH = "Nuvarande arbete";
  private static final String CODE_TWO = "BESOK_ARBETSPLATS";
  private static final String CODE_TWO_MATCH = "Besök på arbetsplatsen";
  private static final String NOT_PROVIDED = "Ej angivet";
  private static final CertificateDataConfigCheckboxMultipleCode CONFIG = CertificateDataConfigCheckboxMultipleCode
      .builder()
      .list(
          List.of(
              CheckboxMultipleCode
                  .builder()
                  .id(CODE_ONE)
                  .label(CODE_ONE_MATCH)
                  .build(),
              CheckboxMultipleCode
                  .builder()
                  .id(CODE_TWO)
                  .label(CODE_TWO_MATCH)
                  .build()
          )
      )
      .build();
  private ValueConverter codeListValueConverter = new CodeListValueConverter();

  private static CertificateDataElement createElement(CertificateDataConfig config,
      CertificateDataValue value) {
    return CertificateDataElement.builder()
        .config(config)
        .value(value)
        .build();
  }

  @Test
  void shouldConvertCertificateDataCodeListWithOneValue() {
    final var elements = createElement(CONFIG,
        CertificateDataValueCodeList.builder()
            .list(
                List.of(
                    CertificateDataValueCode.builder()
                        .code(CODE_ONE)
                        .build()
                )
            )
            .build()
    );

    final var expectedResult = CertificateQuestionValueList.builder()
        .values(
            List.of(
                CODE_ONE_MATCH
            )
        )
        .build();

    final var result = codeListValueConverter.convert(elements);
    assertEquals(expectedResult, result);
  }

  @Test
  void shouldConvertCertificateDataCodeListWithManyValue() {
    final var elements = createElement(CONFIG,
        CertificateDataValueCodeList.builder()
            .list(
                List.of(
                    CertificateDataValueCode.builder()
                        .code(CODE_ONE)
                        .build(),
                    CertificateDataValueCode.builder()
                        .code(CODE_TWO)
                        .build()
                )
            )
            .build()
    );

    final var expectedResult = CertificateQuestionValueList.builder()
        .values(
            List.of(
                CODE_ONE_MATCH,
                CODE_TWO_MATCH
            )
        )
        .build();

    final var result = codeListValueConverter.convert(elements);
    assertEquals(expectedResult, result);
  }

  @Test
  void shouldConvertCertificateDataCodeListIfNoValue() {
    final var elements = createElement(CONFIG,
        CertificateDataValueCodeList.builder()
            .build());

    final var expectedResult = CertificateQuestionValueText.builder()
        .value(NOT_PROVIDED)
        .build();

    final var result = codeListValueConverter.convert(elements);
    assertEquals(expectedResult, result);
  }

  @Test
  void shouldConvertToNoValueIfWrongConfig() {
    final var elements = createElement(CertificateDataConfigTextArea.builder().build(),
        CertificateDataValueCodeList.builder()
            .build());

    final var expectedResult = CertificateQuestionValueText.builder()
        .value(NOT_PROVIDED)
        .build();

    final var result = codeListValueConverter.convert(elements);
    assertEquals(expectedResult, result);
  }
}