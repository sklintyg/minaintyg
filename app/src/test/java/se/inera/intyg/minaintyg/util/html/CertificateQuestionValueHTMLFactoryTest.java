package se.inera.intyg.minaintyg.util.html;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionListValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionTableValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionTextList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.question.CertificateQuestionTextValue;

class CertificateQuestionValueHTMLFactoryTest {

  @Test
  void shouldReturnHTMLForList() {
    final var question = CertificateQuestionListValue
        .builder()
        .title("Title")
        .values(List.of("element 1", "element 2"))
        .build();

    final var result = CertificateQuestionValueHTMLFactory.list(question);

    assertEquals(
        "<h3 className=\"ids-heading-3\">Title</h3><ul><li>element 1</li><li>element 2</li></ul>",
        result);
  }

  @Test
  void shouldReturnHTMLForText() {
    final var question = CertificateQuestionTextValue
        .builder()
        .title("Title")
        .value("element 1")
        .build();

    final var result = CertificateQuestionValueHTMLFactory.text(question);

    assertEquals(
        "<h3 className=\"ids-heading-3\">Title</h3><p>element 1</p>",
        result);
  }

  @Test
  void shouldReturnHTMLForTextList() {
    final var question = CertificateQuestionTextList
        .builder()
        .title("Title")
        .values(Map.of("heading", "element 1"))
        .build();

    final var result = CertificateQuestionValueHTMLFactory.textList(question);

    assertEquals(
        "<h3 className=\"ids-heading-3\">Title</h3><h4 className=\"ids-heading-4\">heading</h4><p>element 1</p>",
        result);
  }

  @Test
  void shouldReturnHTMLForTable() {
    final var question = CertificateQuestionTableValue
        .builder()
        .title("Title")
        .headings(List.of("heading 1", "heading 2"))
        .values(Map.of(1, List.of("Value 1", "Value 2")))
        .build();

    final var result = CertificateQuestionValueHTMLFactory.table(question);

    assertEquals(
        "<h3 className=\"ids-heading-3\">Title</h3><table className=\"ids-table\"><thead><th>heading 1</th><th>heading 2</th></thead><tbody><tr><td>Value 1</td><td>Value 2</td></tr></tbody></table>",
        result);
  }
}