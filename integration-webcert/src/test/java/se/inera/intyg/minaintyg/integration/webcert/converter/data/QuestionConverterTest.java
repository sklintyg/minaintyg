package se.inera.intyg.minaintyg.integration.webcert.converter.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTextArea;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataTextValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@ExtendWith(MockitoExtension.class)
class QuestionConverterTest {

  private static final String TITLE = "title";
  private static final String LABEL_VALUE = "labelValue";
  private static final String NOT_PROVIDED = "Ej angivet";
  private static final String TECHNICAL_ERROR = "Kan inte visa värdet pga tekniskt fel!";
  private static final String CONFIG_ID = "configId";
  private ValueConverter textValueConverter = mock(ValueConverter.class);
  private QuestionConverter questionConverter;

  @BeforeEach
  void setUp() {
    doReturn(CertificateDataValueType.TEXT).when(textValueConverter).getType();
    questionConverter = new QuestionConverter(List.of(textValueConverter));
  }

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
  class Value {

    @Test
    void shallReturnTextValueWithNotProvidedIfValueIsNull() {
      final var expectedValue = CertificateQuestionValueText.builder()
          .value(NOT_PROVIDED)
          .build();

      final var elements = createElement(
          CertificateDataConfigTextArea.builder()
              .build(), null
      );

      final var result = questionConverter.convert(elements);
      assertEquals(expectedValue, result.getValue());
    }

    @Test
    void shallReturnTextValueWithTechnicalErrorIfValueConverterDoesntExist() {
      final var expectedValue = CertificateQuestionValueText.builder()
          .value(TECHNICAL_ERROR)
          .build();

      final var elements = createElement(
          CertificateDataConfigTextArea.builder()
              .build(),
          CertificateDataValueBoolean.builder().build()
      );

      final var result = questionConverter.convert(elements);
      assertEquals(expectedValue, result.getValue());
    }

    @Test
    void shallReturnConvertedValueWhenValueConverterExists() {
      final var expectedValue = CertificateQuestionValueText.builder()
          .value("Converted textvalue")
          .build();

      doReturn(expectedValue).when(textValueConverter).convert(any());

      final var elements = createElement(
          CertificateDataConfigTextArea.builder()
              .build(),
          CertificateDataTextValue.builder().build()
      );

      final var result = questionConverter.convert(elements);
      assertEquals(expectedValue, result.getValue());
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

  private static CertificateDataElement createElement(CertificateDataConfig config,
      CertificateDataValue value) {
    return CertificateDataElement.builder()
        .config(config)
        .value(value)
        .build();
  }
}