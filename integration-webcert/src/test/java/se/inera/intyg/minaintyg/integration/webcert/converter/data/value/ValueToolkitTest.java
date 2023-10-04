package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueToolkit.getValueDateListSubQuestions;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateList;

class ValueToolkitTest {

  private static final String NOT_PROVIDED = "Ej angivet";
  private static final String LABEL = "label";

  @Nested
  class ValueDateListSubQuestions {

    @Test
    void shouldReturnSubQuestions() {
      final var config = CertificateDataConfigCheckboxMultipleDate.builder()
          .list(
              List.of(
                  getCheckboxMultipleDate("1"),
                  getCheckboxMultipleDate("2"),
                  getCheckboxMultipleDate("3"),
                  getCheckboxMultipleDate("4")
              )
          )
          .build();
      final var value = CertificateDataValueDateList.builder()
          .list(
              List.of(
                  getDataValueDate("1"),
                  getDataValueDate("2"),
                  getDataValueDate("3"),
                  getDataValueDate("4")
              )
          )
          .build();
      final var result = getValueDateListSubQuestions(value, config);
      assertFalse(result.isEmpty());
    }

    @Test
    void shouldReturnSubQuestionsWithCorrectLabel() {
      final var config = CertificateDataConfigCheckboxMultipleDate.builder()
          .list(
              List.of(
                  getCheckboxMultipleDate("1"),
                  getCheckboxMultipleDate("2"),
                  getCheckboxMultipleDate("3"),
                  getCheckboxMultipleDate("4")
              )
          )
          .build();
      final var value = CertificateDataValueDateList.builder()
          .list(
              List.of(
                  getDataValueDate("1"),
                  getDataValueDate("2"),
                  getDataValueDate("3"),
                  getDataValueDate("4")
              )
          )
          .build();
      final var result = getValueDateListSubQuestions(value, config);
      assertEquals(LABEL, result.get(0).getLabel());
      assertEquals(LABEL, result.get(1).getLabel());
      assertEquals(LABEL, result.get(2).getLabel());
      assertEquals(LABEL, result.get(3).getLabel());
    }

    @Test
    void shouldReturnSubQuestionsWithAllValues() {
      final var config = CertificateDataConfigCheckboxMultipleDate.builder()
          .list(
              List.of(
                  getCheckboxMultipleDate("1"),
                  getCheckboxMultipleDate("2"),
                  getCheckboxMultipleDate("3"),
                  getCheckboxMultipleDate("4")
              )
          )
          .build();
      final var value = CertificateDataValueDateList.builder()
          .list(
              List.of(
                  getDataValueDate("1"),
                  getDataValueDate("2"),
                  getDataValueDate("3"),
                  getDataValueDate("4")
              )
          )
          .build();
      final var result = getValueDateListSubQuestions(value, config);
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
      final var config = CertificateDataConfigCheckboxMultipleDate.builder()
          .list(
              List.of(
                  getCheckboxMultipleDate("1"),
                  getCheckboxMultipleDate("2"),
                  getCheckboxMultipleDate("3"),
                  getCheckboxMultipleDate("4")
              )
          )
          .build();
      final var value = CertificateDataValueDateList.builder()
          .list(
              List.of(
                  getDataValueDate("3")
              )
          )
          .build();
      final var result = getValueDateListSubQuestions(value, config);
      assertEquals(NOT_PROVIDED,
          ((CertificateQuestionValueText) result.get(0).getValue()).getValue());
      assertEquals(NOT_PROVIDED,
          ((CertificateQuestionValueText) result.get(1).getValue()).getValue());
      assertEquals(LocalDate.now().toString(),
          ((CertificateQuestionValueText) result.get(2).getValue()).getValue());
      assertEquals(NOT_PROVIDED,
          ((CertificateQuestionValueText) result.get(3).getValue()).getValue());
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
