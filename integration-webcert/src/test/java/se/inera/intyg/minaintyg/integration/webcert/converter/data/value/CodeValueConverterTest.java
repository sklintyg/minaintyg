package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter.NOT_PROVIDED_VALUE;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigDropdown;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioMultipleCodeOptionalDropdown;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTextArea;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.DropdownItem;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.RadioMultipleCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.RadioMultipleCodeOptionalDropdown;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

class CodeValueConverterTest {

  private static final String ID_ONE = "ID_ONE";
  private static final String LABEL_ONE = "LABEL_ONE";
  public static final String SUBQUESTION_ID = "SUBQUESTION_ID";
  public static final String ID_TWO = "ID_TWO";
  public static final String LABEL_TWO = "LABEL_TWO";
  public static final String SUBQUESTION_ID_ONE = "SUBQUESTION_ID_ONE";
  public static final String SUBQUESTION_LABEL_ONE = "SUBQUESTION_LABEL_ONE";

  private final ValueConverter codeValueConverter = new CodeValueConverter();

  @Test
  void shallReturnDateValueType() {
    assertEquals(CertificateDataValueType.CODE, codeValueConverter.getType());
  }

  @Test
  void shallReturnIncludeSubquestionsTrue() {
    assertEquals(Boolean.TRUE, codeValueConverter.includeSubquestions());
  }

  @Test
  void shallReturnNotProvidedIfNoValueExists() {
    final var element = CertificateDataElement.builder()
        .config(
            createConfigForOptionalDropdown(
                createOptionalDropdownItem(ID_ONE, LABEL_ONE)
            )
        )
        .value(
            CertificateDataValueCode.builder().build()
        )
        .build();

    final var result = codeValueConverter.convert(element);
    assertEquals(NOT_PROVIDED_VALUE, result);
  }

  @Test
  void shallIncludeDropdownQuestionAnswerIfExists() {
    final var expectedValue = CertificateQuestionValueText.builder()
        .value("%s %s".formatted(LABEL_TWO, SUBQUESTION_LABEL_ONE))
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createConfigForOptionalDropdown(
                createOptionalDropdownItem(ID_TWO, LABEL_TWO, SUBQUESTION_ID)
            ))
        .value(
            createCodeValue(ID_TWO)
        )
        .build();

    final var subQuestions = List.of(
        CertificateDataElement.builder()
            .id(SUBQUESTION_ID)
            .config(
                createConfigDropdown(SUBQUESTION_ID_ONE, SUBQUESTION_LABEL_ONE)
            )
            .value(
                createCodeValue(SUBQUESTION_ID_ONE)
            )
            .build()
    );

    final var actualValue = codeValueConverter.convert(element, subQuestions);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallIncludeDropdownQuestionIdIfNoMatchingSubquestionExists() {
    final var expectedValue = CertificateQuestionValueText.builder()
        .value("%s %s".formatted(LABEL_TWO, SUBQUESTION_ID))
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createConfigForOptionalDropdown(
                createOptionalDropdownItem(ID_TWO, LABEL_TWO, SUBQUESTION_ID)
            )
        )
        .value(
            createCodeValue(ID_TWO)
        )
        .build();

    final var actualValue = codeValueConverter.convert(element, Collections.emptyList());
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallIncludeCodeLabelWhenLabelExists() {
    final var expectedValue = CertificateQuestionValueText.builder()
        .value(LABEL_ONE)
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createConfigForOptionalDropdown(
                createOptionalDropdownItem(ID_ONE, LABEL_ONE)
            )
        )
        .value(
            createCodeValue(ID_ONE)
        )
        .build();

    final var result = codeValueConverter.convert(element);
    assertEquals(expectedValue, result);
  }

  @Test
  void shallIncludeCodeIdWhenLabelExists() {
    final var expectedValue = CertificateQuestionValueText.builder()
        .value(ID_ONE)
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createConfigForOptionalDropdown()
        )
        .value(
            createCodeValue(ID_ONE)
        )
        .build();

    final var result = codeValueConverter.convert(element);
    assertEquals(expectedValue, result);
  }

  @Test
  void shallIncludeCodeIdWhenConfigurationIsntSupported() {
    final var expectedValue = CertificateQuestionValueText.builder()
        .value(ID_ONE)
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            CertificateDataConfigTextArea.builder().build()
        )
        .value(
            createCodeValue(ID_ONE)
        )
        .build();

    final var result = codeValueConverter.convert(element);
    assertEquals(expectedValue, result);
  }

  @Test
  void shallIncludeCodeWithRadioMultipleCodeConfig() {
    final var expectedValue = CertificateQuestionValueText.builder()
        .value(LABEL_ONE)
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            CertificateDataConfigRadioMultipleCode.builder()
                .list(
                    List.of(
                        RadioMultipleCode.builder()
                            .id(ID_ONE)
                            .label(LABEL_ONE)
                            .build()
                    )
                )
                .build()
        )
        .value(
            createCodeValue(ID_ONE)
        )
        .build();
    final var result = codeValueConverter.convert(element);
    assertEquals(expectedValue, result);
  }

  @Test
  void shallIncludeCodeWhenIdIsNotFoundInRadioMultipleCodeConfig() {
    final var expectedValue = CertificateQuestionValueText.builder()
        .value(ID_ONE)
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            CertificateDataConfigRadioMultipleCode.builder()
                .list(
                    List.of(
                        RadioMultipleCode.builder()
                            .id(ID_TWO)
                            .label(LABEL_ONE)
                            .build()
                    )
                )
                .build()
        )
        .value(
            createCodeValue(ID_ONE)
        )
        .build();
    final var result = codeValueConverter.convert(element);
    assertEquals(expectedValue, result);
  }

  private static RadioMultipleCodeOptionalDropdown createOptionalDropdownItem(String id,
      String label, String questionId) {
    return RadioMultipleCodeOptionalDropdown.builder()
        .id(id)
        .label(label)
        .dropdownQuestionId(questionId)
        .build();
  }

  private static RadioMultipleCodeOptionalDropdown createOptionalDropdownItem(String id,
      String label) {
    return createOptionalDropdownItem(id, label, null);
  }

  private static CertificateDataValueCode createCodeValue(String code) {
    return CertificateDataValueCode.builder()
        .id(code)
        .code(code)
        .build();
  }

  private static CertificateDataConfigDropdown createConfigDropdown(String id, String label) {
    return CertificateDataConfigDropdown.builder()
        .list(
            List.of(
                DropdownItem.builder()
                    .id(id)
                    .label(label)
                    .build()
            )
        )
        .build();
  }

  private static CertificateDataConfigRadioMultipleCodeOptionalDropdown createConfigForOptionalDropdown(
      RadioMultipleCodeOptionalDropdown... dropdownConfig) {
    return CertificateDataConfigRadioMultipleCodeOptionalDropdown.builder()
        .list(
            List.of(
                dropdownConfig
            )
        )
        .build();
  }
}