package se.inera.intyg.minaintyg.certificate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueGeneralTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.TableElement;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.TableElementType;

@ExtendWith(MockitoExtension.class)
class FormattedQuestionConverterTest {

  @InjectMocks
  FormattedQuestionConverter formattedQuestionConverter;

  @Test
  void shouldReturnHTMLForList() {
    final var question = CertificateQuestion.builder()
        .title("Title")
        .label("Label")
        .value(
            CertificateQuestionValueList
                .builder()
                .values(List.of("element 1", "element 2"))
                .build())
        .build();

    final var result = formattedQuestionConverter.convert(question);

    assertEquals(
        "<h3 className=\"ids-heading-3\">Title</h3><h4 className=\"ids-heading-4\">Label</h4><ul><li>element 1</li><li>element 2</li></ul>",
        result);
  }

  @Test
  void shouldReturnHTMLWithoutLabel() {
    final var question = CertificateQuestion.builder()
        .title("Title")
        .value(
            CertificateQuestionValueList
                .builder()
                .values(List.of("element 1", "element 2"))
                .build())
        .build();

    final var result = formattedQuestionConverter.convert(question);

    assertEquals(
        "<h3 className=\"ids-heading-3\">Title</h3><ul><li>element 1</li><li>element 2</li></ul>",
        result);
  }

  @Test
  void shouldReturnHTMLWithLabelButWithoutTitle() {
    final var question = CertificateQuestion.builder()
        .label("Label")
        .value(
            CertificateQuestionValueList
                .builder()
                .values(List.of("element 1", "element 2"))
                .build())
        .build();

    final var result = formattedQuestionConverter.convert(question);

    assertEquals(
        "<h3 className=\"ids-heading-3\">Label</h3><ul><li>element 1</li><li>element 2</li></ul>",
        result);
  }

  @Test
  void shouldReturnHTMLWithHeader() {
    final var question = CertificateQuestion.builder()
        .title("Title")
        .header("Header")
        .label("Label")
        .value(
            CertificateQuestionValueList
                .builder()
                .values(List.of("element 1", "element 2"))
                .build())
        .build();

    final var result = formattedQuestionConverter.convert(question);

    assertEquals(
        "<h3 className=\"ids-heading-3\">Header</h3><h4 className=\"ids-heading-4\">Title</h4><h4 className=\"ids-heading-4\">Label</h4><ul><li>element 1</li><li>element 2</li></ul>",
        result);
  }

  @Test
  void shouldReturnHTMLForText() {
    final var question = CertificateQuestion.builder()
        .title("Title")
        .label("Label")
        .value(
            CertificateQuestionValueText
                .builder()
                .value("element 1")
                .build()
        )
        .build();

    final var result = formattedQuestionConverter.convert(question);

    assertEquals(
        "<h3 className=\"ids-heading-3\">Title</h3><h4 className=\"ids-heading-4\">Label</h4><p>element 1</p>",
        result);
  }

  @Test
  void shouldReturnHTMLWithSubQuestion() {
    final var question = CertificateQuestion.builder()
        .title("Title")
        .label("Label")
        .value(
            CertificateQuestionValueText
                .builder()
                .value("element 1")
                .build()
        )
        .build();

    final var completeQuestion = CertificateQuestion.builder()
        .subQuestions(List.of(question))
        .title("Complete title")
        .label("Complete label")
        .value(
            CertificateQuestionValueText.builder().value("Complete text").build()
        )
        .build();

    final var result = formattedQuestionConverter.convert(completeQuestion);

    assertEquals(
        "<h3 className=\"ids-heading-3\">Complete title</h3><h4 className=\"ids-heading-4\">Complete label</h4><p>Complete text</p><h4 className=\"ids-heading-4\">Title</h4><h5 className=\"ids-heading-5\">Label</h5><p>element 1</p>",
        result);
  }

  @Test
  void shouldReturnHTMLWithSeveralLayersOfSubQuestions() {
    final var question = CertificateQuestion.builder()
        .title("Title")
        .label("Label")
        .value(
            CertificateQuestionValueText
                .builder()
                .value("element 1")
                .build()
        )
        .subQuestions(List.of(
                CertificateQuestion.builder()
                    .title("Title sub question 2")
                    .label("Label sub question 2")
                    .value(
                        CertificateQuestionValueText.builder().value("element 2").build()
                    )
                    .build()
            )
        )
        .build();

    final var completeQuestion = CertificateQuestion.builder()
        .subQuestions(List.of(question))
        .title("Complete title")
        .label("Complete label")
        .value(
            CertificateQuestionValueText.builder().value("Complete text").build()
        )
        .build();

    final var result = formattedQuestionConverter.convert(completeQuestion);

    assertEquals(
        "<h3 className=\"ids-heading-3\">Complete title</h3><h4 className=\"ids-heading-4\">Complete label</h4><p>Complete text</p><h4 className=\"ids-heading-4\">Title</h4><h5 className=\"ids-heading-5\">Label</h5><p>element 1</p><h4 className=\"ids-heading-4\">Title sub question 2</h4><h5 className=\"ids-heading-5\">Label sub question 2</h5><p>element 2</p>",
        result);
  }

  @Test
  void shouldReturnHTMLWithLabelButWithoutTitleForSubQuestion() {
    final var question = CertificateQuestion.builder()
        .subQuestions(
            List.of(
                CertificateQuestion.builder()
                    .label("Label")
                    .value(
                        CertificateQuestionValueList
                            .builder()
                            .values(List.of("element 1", "element 2"))
                            .build()
                    )
                    .build()
            )
        )
        .build();

    final var result = formattedQuestionConverter.convert(question);

    assertEquals(
        "<h4 className=\"ids-heading-4\">Label</h4><ul><li>element 1</li><li>element 2</li></ul>",
        result);
  }

  @Test
  void shouldReturnHTMLForTable() {
    final var question = CertificateQuestion.builder()
        .title("Title")
        .label("Label")
        .value(
            CertificateQuestionValueTable
                .builder()
                .headings(List.of("heading 1", "heading 2"))
                .values(List.of(List.of("Value 1", "Value 2")))
                .build()
        )
        .build();

    final var result = formattedQuestionConverter.convert(question);

    assertEquals(
        "<h3 className=\"ids-heading-3\">Title</h3><h4 className=\"ids-heading-4\">Label</h4><table className=\"ids-table\"><thead><th>heading 1</th><th>heading 2</th></thead><tbody><tr><td>Value 1</td><td>Value 2</td></tr></tbody></table>",
        result);
  }

  @Test
  void shouldReturnHTMLForGeneralTable() {
    final var question = CertificateQuestion.builder()
        .title("Title")
        .label("Label")
        .value(
            CertificateQuestionValueGeneralTable
                .builder()
                .headings(
                    List.of(
                        TableElement.builder().type(TableElementType.DATA).value("").build(),
                        TableElement.builder().type(TableElementType.HEADING).value("h 1").build(),
                        TableElement.builder().type(TableElementType.HEADING).value("h 2").build()
                    )
                )
                .values(List.of(
                    List.of(
                        TableElement.builder().type(TableElementType.HEADING).value("h 3").build(),
                        TableElement.builder().type(TableElementType.DATA).value("d 1").build(),
                        TableElement.builder().type(TableElementType.DATA).value("d 2").build()
                    )
                ))
                .build()
        )
        .build();

    final var result = formattedQuestionConverter.convert(question);

    assertEquals(
        "<h3 className=\"ids-heading-3\">Title</h3><h4 className=\"ids-heading-4\">Label</h4><table className=\"ids-table\"><thead><tr><td></td><th>h 1</th><th>h 2</th></tr></thead><tbody><tr><th>h 3</th><td>d 1</td><td>d 2</td></tr></tbody></table>",
        result);
  }

  @Test
  void shouldReturnTitleForQuestionWithNoValue() {
    final var question = CertificateQuestion.builder()
        .title("Title")
        .label("Label")
        .build();

    final var result = formattedQuestionConverter.convert(question);

    assertEquals(
        "<h3 className=\"ids-heading-3\">Title</h3><h4 className=\"ids-heading-4\">Label</h4>",
        result);
  }
}