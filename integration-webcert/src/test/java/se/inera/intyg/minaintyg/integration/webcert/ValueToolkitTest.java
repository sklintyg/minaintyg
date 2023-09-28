package se.inera.intyg.minaintyg.integration.webcert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getValueBoolean;
import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getValueDateListSubQuestions;
import static se.inera.intyg.minaintyg.integration.webcert.ValueToolkit.getValueText;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataTextValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateList;

class ValueToolkitTest {

  private static final String NOT_PROVIDED = "Ej angivet";
  private static final String TRUE_BOOLEAN = "Ja";
  private static final String FALSE_BOOLEAN = "Nej";
  private static final String TEXT = "text";
  private static final String LABEL = "label";

  @Nested
  class ValueBoolean {

    @Test
    void shouldHandleNull() {
      final var valueBoolean = CertificateDataValueBoolean.builder().build();
      final var result = getValueBoolean(valueBoolean);
      assertEquals(NOT_PROVIDED, result.getValue());
    }

    @Test
    void shouldHandleTrue() {
      final var valueBoolean = CertificateDataValueBoolean.builder()
          .selected(true)
          .build();
      final var result = getValueBoolean(valueBoolean);
      assertEquals(TRUE_BOOLEAN, result.getValue());
    }

    @Test
    void shouldHandleFalse() {
      final var valueBoolean = CertificateDataValueBoolean.builder()
          .selected(false)
          .build();
      final var result = getValueBoolean(valueBoolean);
      assertEquals(FALSE_BOOLEAN, result.getValue());
    }
  }

  @Nested
  class ValueText {

    @Test
    void shouldHandleNull() {
      final var valueText = CertificateDataTextValue.builder().build();
      final var result = getValueText(valueText);
      assertEquals(NOT_PROVIDED, result.getValue());
    }


    @Test
    void shouldReturnTextValue() {
      final var valueText = CertificateDataTextValue.builder()
          .text(TEXT)
          .build();
      final var result = getValueText(valueText);
      assertEquals(TEXT, result.getValue());
    }
  }

  @Nested
  class ValueDateListSubQuestions {

    @Test
    void shouldReturnNullIfWrongConfig() {
      final var dataElement = CertificateDataElement.builder()
          .config(
              CertificateDataConfigRadioBoolean.builder().build()
          )
          .build();
      final var result = getValueDateListSubQuestions(dataElement);
      assertNull(result);
    }

    @Test
    void shouldReturnSubQuestions() {
      final var dataElement = getElemetsWithMultipleDateConfigAndDateListValue();

      final var result = getValueDateListSubQuestions(dataElement);
      assertFalse(result.isEmpty());
    }

    @Test
    void shouldReturnSubQuestionsWithCorrectLabel() {
      final var dataElement = getElemetsWithMultipleDateConfigAndDateListValue();

      final var result = getValueDateListSubQuestions(dataElement);
      assertEquals(LABEL, result.get(0).getLabel());
      assertEquals(LABEL, result.get(1).getLabel());
      assertEquals(LABEL, result.get(2).getLabel());
      assertEquals(LABEL, result.get(3).getLabel());
    }

    @Test
    void shouldReturnSubQuestionsWithAllValues() {
      final var dataElement = getElemetsWithMultipleDateConfigAndDateListValue();

      final var result = getValueDateListSubQuestions(dataElement);
      assertEquals(LocalDate.now().toString(),
          ((CertificateQuestionValueText) result.get(0).getValue()).getValue());
      assertEquals(LocalDate.now().toString(),
          ((CertificateQuestionValueText) result.get(1).getValue()).getValue());
      assertEquals(LocalDate.now().toString(),
          ((CertificateQuestionValueText) result.get(2).getValue()).getValue());
      assertEquals(LocalDate.now().toString(),
          ((CertificateQuestionValueText) result.get(3).getValue()).getValue());
    }

    @Test
    void shouldReturnSubQuestionsWithOneValue() {
      final var dataElement = getElemetsWithMultipleDateConfigAndOneDateValue();

      final var result = getValueDateListSubQuestions(dataElement);
      assertEquals(NOT_PROVIDED,
          ((CertificateQuestionValueText) result.get(0).getValue()).getValue());
      assertEquals(NOT_PROVIDED,
          ((CertificateQuestionValueText) result.get(1).getValue()).getValue());
      assertEquals(LocalDate.now().toString(),
          ((CertificateQuestionValueText) result.get(2).getValue()).getValue());
      assertEquals(NOT_PROVIDED,
          ((CertificateQuestionValueText) result.get(3).getValue()).getValue());
    }

    private CertificateDataElement getElemetsWithMultipleDateConfigAndDateListValue() {
      return CertificateDataElement.builder()
          .config(
              CertificateDataConfigCheckboxMultipleDate.builder()
                  .list(
                      List.of(
                          getCheckboxMultipleDate("1"),
                          getCheckboxMultipleDate("2"),
                          getCheckboxMultipleDate("3"),
                          getCheckboxMultipleDate("4")
                      )
                  )
                  .build()
          )
          .value(
              CertificateDataValueDateList.builder()
                  .list(
                      List.of(
                          getDataValueDate("1"),
                          getDataValueDate("2"),
                          getDataValueDate("3"),
                          getDataValueDate("4")
                      )
                  )
                  .build()
          )
          .build();
    }

    private CertificateDataElement getElemetsWithMultipleDateConfigAndOneDateValue() {
      return CertificateDataElement.builder()
          .config(
              CertificateDataConfigCheckboxMultipleDate.builder()
                  .list(
                      List.of(
                          getCheckboxMultipleDate("1"),
                          getCheckboxMultipleDate("2"),
                          getCheckboxMultipleDate("3"),
                          getCheckboxMultipleDate("4")
                      )
                  )
                  .build()
          )
          .value(
              CertificateDataValueDateList.builder()
                  .list(
                      List.of(getDataValueDate("3"))
                  )
                  .build()
          )
          .build();
    }

    private CertificateDataValueDate getDataValueDate(String id) {
      return CertificateDataValueDate.builder()
          .id(id)
          .date(LocalDate.now())
          .build();
    }

    private CheckboxMultipleDate getCheckboxMultipleDate(String id) {
      return CheckboxMultipleDate.builder()
          .id(id)
          .label(LABEL)
          .build();
    }
  }
}