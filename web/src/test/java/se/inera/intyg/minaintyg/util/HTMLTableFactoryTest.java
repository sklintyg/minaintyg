package se.inera.intyg.minaintyg.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.util.html.HTMLTableFactory;

class HTMLTableFactoryTest {

  @Test
  void shouldReturnTableWithOneTr() {
    final var result = HTMLTableFactory.table(
        Map.of(1, List.of("Value 1", "Value 2")),
        List.of("heading 1", "heading 2")
    );

    assertEquals(
        "<table className=\"ids-table\"><thead><th>heading 1</th><th>heading 2</th></thead><tbody><tr><td>Value 1</td><td>Value 2</td></tr></tbody></table>",
        result);
  }

  @Test
  void shouldReturnUlWithTwoTr() {
    final var result = HTMLTableFactory.table(
        Map.of(1, List.of("Value 1", "Value 2"), 2, List.of("Value 3", "Value 4")),
        List.of("heading 1", "heading 2")
    );

    assertEquals(
        "<table className=\"ids-table\"><thead><th>heading 1</th><th>heading 2</th></thead><tbody><tr><td>Value 1</td><td>Value 2</td></tr><tr><td>Value 3</td><td>Value 4</td></tr></tbody></table>",
        result);
  }
}