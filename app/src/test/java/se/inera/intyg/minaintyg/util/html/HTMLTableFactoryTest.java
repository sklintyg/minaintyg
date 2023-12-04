package se.inera.intyg.minaintyg.util.html;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.TableElement;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.TableElementType;

class HTMLTableFactoryTest {

  @Test
  void shouldReturnTableWithOneTr() {
    final var result = HTMLTableFactory.table(
        List.of(List.of("Value 1", "Value 2")),
        List.of("heading 1", "heading 2")
    );

    assertEquals(
        "<table className=\"ids-table\"><thead><th>heading 1</th><th>heading 2</th></thead><tbody><tr><td>Value 1</td><td>Value 2</td></tr></tbody></table>",
        result);
  }

  @Test
  void shouldReturnGeneralTableWithOneTr() {
    final var result = HTMLTableFactory.table(
        List.of(
            List.of(
                TableElement.builder().type(TableElementType.HEADING).value("heading").build(),
                TableElement.builder().type(TableElementType.DATA).value("data").build()
            )
        )
    );

    assertEquals(
        "<table className=\"ids-table\"><tbody><tr><th>heading</th><td>data</td></tr></tbody></table>",
        result);
  }

  @Test
  void shouldReturnTableWithTwoTr() {
    final var result = HTMLTableFactory.table(
        List.of(List.of("Value 1", "Value 2"), List.of("Value 3", "Value 4")),
        List.of("heading 1", "heading 2")
    );

    assertEquals(
        "<table className=\"ids-table\"><thead><th>heading 1</th><th>heading 2</th></thead><tbody><tr><td>Value 1</td><td>Value 2</td></tr><tr><td>Value 3</td><td>Value 4</td></tr></tbody></table>",
        result);
  }
}