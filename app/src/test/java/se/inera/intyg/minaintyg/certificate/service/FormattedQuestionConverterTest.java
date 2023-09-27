package se.inera.intyg.minaintyg.certificate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;

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
  void shouldReturnHTMLWithoutTitle() {
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
        "<h4 className=\"ids-heading-4\">Label</h4><ul><li>element 1</li><li>element 2</li></ul>",
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
}