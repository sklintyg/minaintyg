package se.inera.intyg.minaintyg.util.html;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueGeneralTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueItemList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificationQuestionValueItem;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.TableElement;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.TableElementType;

class CertificateQuestionValueHTMLFactoryTest {

  @Test
  void shouldReturnHTMLForList() {
    final var question = CertificateQuestionValueList
        .builder()
        .values(List.of("element 1", "element 2"))
        .build();

    final var result = CertificateQuestionValueHTMLFactory.list(question);

    assertEquals(
        "<ul><li>element 1</li><li>element 2</li></ul>",
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
        "<p>element 1</p>",
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
        "<table className=\"ids-table\"><thead><th>heading 1</th><th>heading 2</th></thead><tbody><tr><td>Value 1</td><td>Value 2</td></tr></tbody></table>",
        result);
  }

  @Test
  void shouldReturnHTMLForGeneralTable() {
    final var value = CertificateQuestionValueGeneralTable
        .builder()
        .values(List.of(
            List.of(
                TableElement.builder().type(TableElementType.DATA).value("").build(),
                TableElement.builder().type(TableElementType.HEADING).value("h 1").build(),
                TableElement.builder().type(TableElementType.HEADING).value("h 2").build()
            ),
            List.of(
                TableElement.builder().type(TableElementType.HEADING).value("h 3").build(),
                TableElement.builder().type(TableElementType.DATA).value("d 1").build(),
                TableElement.builder().type(TableElementType.DATA).value("d 2").build()
            )
        )).build();

    final var result = CertificateQuestionValueHTMLFactory.table(value);

    assertEquals(
        "<table className=\"ids-table\"><tbody><tr><td></td><th>h 1</th><th>h 2</th></tr><tr><th>h 3</th><td>d 1</td><td>d 2</td></tr></tbody></table>",
        result);
  }

  @Test
  void shouldReturnHTMLForItemList() {
    final var value = CertificateQuestionValueItemList
        .builder()
        .values(List.of(
            CertificationQuestionValueItem.builder()
                .label("label 1")
                .value("value 1")
                .build(),
            CertificationQuestionValueItem.builder()
                .label("label 2")
                .value("value 2")
                .build()
        ))
        .build();

    final var result = CertificateQuestionValueHTMLFactory.itemList(value);

    assertEquals(
        "<h4 className=\"ids-heading-4\">label 1</h4><p>value 1</p><h4 className=\"ids-heading-4\">label 2</h4><p>value 2</p>",
        result);
  }
}