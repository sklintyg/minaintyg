package se.inera.intyg.minaintyg.integration.webcert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigDiagnoses;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioMultipleCodeOptionalDropdown;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTextArea;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.DiagnosesTerminology;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.RadioMultipleCodeOptionalDropdown;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataIcfValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataTextValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCodeList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosis;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDiagnosisList;

@ExtendWith(MockitoExtension.class)
class QuestionConverterTest {

  private static final String TITLE = "title";
  private static final String TEXT_VALUE = "textValue";
  private static final String LABEL_VALUE = "labelValue";
  private static final String NOT_PROVIDED = "Ej angivet";
  private static final String TRUE_BOOLEAN = "Ja";
  private static final String FALSE_BOOLEAN = "Nej";
  private static final String CONFIG_ID = "configId";
  private static final String CODE = "NUVARANDE_ARBETE";
  private static final String CODE_MATCH = "Nuvarande arbete";

  @InjectMocks
  private QuestionConverter questionConverter;

  @Nested
  class ToCertificateQuestion {

    @Nested
    class Title {

      @Test
      void shouldIncludeTitleIfPresent() {
        final var elements = createElement(
            CertificateDataConfigTextArea.builder()
                .text(TITLE)
                .build(), null
        );

        final var result = questionConverter.convert(elements);
        assertEquals(TITLE, result.getTitle());
      }

      @Test
      void shouldNotIncludeTitleIfNotPresent() {
        final var elements = createElement(
            CertificateDataConfigTextArea.builder()
                .build(), null
        );
        final var result = questionConverter.convert(elements);
        assertNull(result.getTitle());
      }
    }

    @Nested
    class Label {

      @Test
      void shouldIncludeLabelIfPresent() {
        final var elements = createElement(
            CertificateDataConfigTextArea.builder()
                .label(LABEL_VALUE)
                .build(), null
        );
        final var result = questionConverter.convert(elements);
        assertEquals(LABEL_VALUE, result.getLabel());
      }

      @Test
      void shouldNotIncludeLabelIfNotPresent() {
        final var elements = createElement(
            CertificateDataConfigTextArea.builder()
                .build(), null
        );

        final var result = questionConverter.convert(elements);
        assertNull(result.getLabel());
      }
    }

    @Nested
    class TextValue {

      @Test
      void shouldConvertCertificateDataTextValue() {
        final var elements = createElement(
            CertificateDataConfigTextArea.builder().build(),
            CertificateDataTextValue.builder()
                .text(TEXT_VALUE)
                .build()
        );

        final var expectedResult = CertificateQuestion.builder()
            .value(CertificateQuestionValueText.builder()
                .value(TEXT_VALUE)
                .build())
            .build();

        final var result = questionConverter.convert(elements);
        assertEquals(expectedResult, result);
      }

      @Test
      void shouldConvertCertificateDataTextValueWithNoValue() {
        final var elements = createElement(
            CertificateDataConfigTextArea.builder().build(),
            CertificateDataTextValue.builder()
                .build()
        );

        final var expectedResult = CertificateQuestion.builder()
            .value(CertificateQuestionValueText.builder()
                .value(NOT_PROVIDED)
                .build())
            .build();

        final var result = questionConverter.convert(elements);
        assertEquals(expectedResult, result);
      }
    }

    @Nested
    class RadioBoolean {

      @Test
      void shouldConvertCertificateDataValueRadioBooleanWithValueTrue() {
        final var elements = createElement(CertificateDataConfigRadioBoolean.builder().build(),
            CertificateDataValueBoolean.builder()
                .selected(true)
                .build()
        );

        final var expectedResult = CertificateQuestion.builder()
            .value(
                CertificateQuestionValueText.builder()
                    .value(TRUE_BOOLEAN)
                    .build())
            .build();

        final var result = questionConverter.convert(elements);
        assertEquals(expectedResult, result);
      }

      @Test
      void shouldConvertCertificateDataValueRadioBooleanWithValueFalse() {
        final var elements = createElement(CertificateDataConfigRadioBoolean.builder().build(),
            CertificateDataValueBoolean.builder()
                .selected(false)
                .build());

        final var expectedResult = CertificateQuestion.builder()
            .value(
                CertificateQuestionValueText.builder()
                    .value(FALSE_BOOLEAN)
                    .build())
            .build();

        final var result = questionConverter.convert(elements);
        assertEquals(expectedResult, result);
      }

      @Test
      void shouldConvertCertificateDataValueRadioBooleanWithNoValue() {
        final var elements = createElement(CertificateDataConfigRadioBoolean.builder().build(),
            CertificateDataValueBoolean.builder()
                .build());

        final var expectedResult = CertificateQuestion.builder()
            .value(
                CertificateQuestionValueText.builder()
                    .value(NOT_PROVIDED)
                    .build())
            .build();

        final var result = questionConverter.convert(elements);
        assertEquals(expectedResult, result);
      }
    }

    @Nested
    class DateList {

      @Test
      void shouldConvertCertificateDataDateList() {
        final var elements = createElement(
            CertificateDataConfigCheckboxMultipleDate.builder()
                .label(LABEL_VALUE)
                .list(
                    List.of(
                        CheckboxMultipleDate.builder()
                            .id(CONFIG_ID)
                            .build()
                    )
                )
                .build(),
            CertificateDataValueDateList.builder()
                .list(
                    List.of(
                        CertificateDataValueDate.builder()
                            .id(CONFIG_ID)
                            .date(LocalDate.now())
                            .build()
                    )
                )
                .build()
        );

        final var expectedResult = CertificateQuestion.builder()
            .label(LABEL_VALUE)
            .value(
                CertificateQuestionValueText.builder().build()
            )
            .subQuestions(
                List.of(
                    CertificateQuestion.builder()
                        .value(CertificateQuestionValueText.builder()
                            .value(LocalDate.now().toString())
                            .build())
                        .build()
                )
            ).build();

        final var result = questionConverter.convert(elements);
        assertEquals(expectedResult, result);
      }

      @Test
      void shouldConvertCertificateDataDateListWithNoValue() {
        final var elements = createElement(
            CertificateDataConfigCheckboxMultipleDate.builder()
                .label(LABEL_VALUE)
                .list(
                    List.of(
                        CheckboxMultipleDate.builder()
                            .id(CONFIG_ID)
                            .build()
                    )
                )
                .build(),
            CertificateDataValueDateList.builder()
                .build()
        );

        final var expectedResult = CertificateQuestion.builder()
            .label(LABEL_VALUE)
            .value(
                CertificateQuestionValueText.builder().build()
            )
            .subQuestions(
                List.of(
                    CertificateQuestion.builder()
                        .value(CertificateQuestionValueText.builder()
                            .value(NOT_PROVIDED)
                            .build())
                        .build()
                )
            ).build();

        final var result = questionConverter.convert(elements);
        assertEquals(expectedResult, result);
      }
    }

    @Nested
    class CodeList {

      @Test
      void shouldConvertCertificateDataCodeList() {
        final var elements = createElement(CertificateDataConfigTextArea.builder().build(),
            CertificateDataValueCodeList.builder()
                .list(
                    List.of(
                        CertificateDataValueCode.builder()
                            .code(CODE)
                            .build()
                    )
                )
                .build()
        );

        final var expectedResult = CertificateQuestion.builder()
            .value(
                CertificateQuestionValueList.builder()
                    .values(
                        List.of(
                            CODE_MATCH
                        )
                    )
                    .build())
            .build();

        final var result = questionConverter.convert(elements);
        assertEquals(expectedResult, result);
      }

      @Test
      void shouldConvertCertificateDataCodeListIfNoValue() {
        final var elements = createElement(CertificateDataConfigTextArea.builder().build(),
            CertificateDataValueCodeList.builder()
                .build());

        final var expectedResult = CertificateQuestion.builder()
            .value(
                CertificateQuestionValueList.builder()
                    .values(
                        List.of(
                            NOT_PROVIDED
                        )
                    )
                    .build())
            .build();

        final var result = questionConverter.convert(elements);
        assertEquals(expectedResult, result);
      }
    }

    @Nested
    class DiagnosisList {

      private static final String DIAGNOSIS_CODE_M79 = "M79";
      private static final String DIAGNOSIS_CODE_M79_DESCRIPTION = "M79 description";
      private static final String DIAGNOSIS_CODE_B36 = "B36";
      private static final String DIAGNOSIS_CODE_B36_DESCRIPTION = "B36 description";

      @Test
      void shouldConvertCertificateDataDiagnosisListValue() {
        final var config = CertificateDataConfigDiagnoses.builder()
            .terminology(
                List.of(
                    DiagnosesTerminology.builder()
                        .label(LABEL_VALUE)
                        .build(),

                    DiagnosesTerminology.builder()
                        .label(LABEL_VALUE)
                        .build()
                )
            )
            .build();

        final var value = CertificateDataValueDiagnosisList.builder()
            .list(
                List.of(
                    CertificateDataValueDiagnosis.builder()
                        .code(DIAGNOSIS_CODE_B36)
                        .description(DIAGNOSIS_CODE_B36_DESCRIPTION)
                        .build(),
                    CertificateDataValueDiagnosis.builder()
                        .code(DIAGNOSIS_CODE_M79)
                        .description(DIAGNOSIS_CODE_M79_DESCRIPTION)
                        .build()
                )
            )
            .build();

        final var expectedResult = CertificateQuestion.builder()
            .value(
                CertificateQuestionValueTable.builder()
                    .headings(
                        List.of(
                            LABEL_VALUE, LABEL_VALUE
                        )
                    )
                    .values(
                        List.of(
                            List.of(
                                DIAGNOSIS_CODE_B36, DIAGNOSIS_CODE_B36_DESCRIPTION
                            ),
                            List.of(
                                DIAGNOSIS_CODE_M79, DIAGNOSIS_CODE_M79_DESCRIPTION
                            )
                        )
                    )
                    .build()
            )
            .build();
        final var elements = createElement(config, value);
        final var result = questionConverter.convert(elements);
        assertEquals(expectedResult, result);
      }
    }

    @Nested
    class IcfValue {

      @Test
      void shouldConvertCertificateIcfValue() {
        final var value = CertificateDataIcfValue.builder()
            .text(TEXT_VALUE)
            .build();
        final var config = CertificateDataConfigTextArea.builder().build();
        final var element = createElement(config, value);
        final var expectedValue = CertificateQuestion.builder()
            .value(
                CertificateQuestionValueText.builder()
                    .value(TEXT_VALUE)
                    .build()
            )
            .build();
        final var result = questionConverter.convert(element);
        assertEquals(expectedValue, result);
      }

      @Test
      void shouldConvertCertificateIcfNoValue() {
        final var value = CertificateDataIcfValue.builder().build();
        final var config = CertificateDataConfigTextArea.builder().build();
        final var element = createElement(config, value);
        final var expectedValue = CertificateQuestion.builder()
            .value(
                CertificateQuestionValueText.builder()
                    .value(NOT_PROVIDED)
                    .build()
            )
            .build();
        final var result = questionConverter.convert(element);
        assertEquals(expectedValue, result);
      }
    }

    @Nested
    class ValueCode {

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
        final var expectedValue = CertificateQuestion.builder()
            .value(
                CertificateQuestionValueText.builder()
                    .value(LABEL_VALUE)
                    .build()
            )
            .build();
        final var result = questionConverter.convert(element);
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
        final var expectedValue = CertificateQuestion.builder()
            .value(
                CertificateQuestionValueText.builder()
                    .value(NOT_PROVIDED)
                    .build()
            )
            .build();
        final var result = questionConverter.convert(element);
        assertEquals(expectedValue, result);
      }
    }
  }

  private static CertificateDataElement createElement(CertificateDataConfig config,
      CertificateDataValue value) {
    return CertificateDataElement.builder()
        .config(config)
        .value(value)
        .build();
  }
}