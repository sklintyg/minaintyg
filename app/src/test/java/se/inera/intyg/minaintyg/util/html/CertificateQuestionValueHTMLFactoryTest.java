package se.inera.intyg.minaintyg.util.html;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;

class CertificateQuestionValueHTMLFactoryTest {

  @Test
  void shouldReturnHTMLForList() {
    final var question = CertificateQuestionValueList
        .builder()
        .values(List.of("element 1", "element 2"))
        .build();

    final var result = CertificateQuestionValueHTMLFactory.list(question);

    assertEquals(
        "<h3 className=\"ids-heading-3\">Title</h3><ul><li>element 1</li><li>element 2</li></ul>",
        result);
  }

  @Test
  void shouldReturnHTMLForText() {
    final var question = CertificateQuestionValueText
        .builder()
        .value("element 1")
        .build();

    final var result = CertificateQuestionValueHTMLFactory.text(question);

    assertEquals(
        "<h3 className=\"ids-heading-3\">Title</h3><p>element 1</p>",
        result);
  }

  @Test
  void shouldReturnHTMLForTable() {
    final var question = CertificateQuestionValueTable
        .builder()
        .headings(List.of("heading 1", "heading 2"))
        .values(List.of(List.of("Value 1", "Value 2")))
        .build();

    final var result = CertificateQuestionValueHTMLFactory.table(question);

    assertEquals(
        "<h3 className=\"ids-heading-3\">Title</h3><table className=\"ids-table\"><thead><th>heading 1</th><th>heading 2</th></thead><tbody><tr><td>Value 1</td><td>Value 2</td></tr></tbody></table>",
        result);
  }
}