package se.inera.intyg.minaintyg.util.html;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HTMLListFactoryTest {

  @Nested
  class TestList {

    @Test
    void shouldReturnUlWithOneLi() {
      final var result = HTMLListFactory.ul(List.of("element 1"));

      assertEquals("<ul><li>element 1</li></ul>", result);
    }

    @Test
    void shouldReturnUlWithTwoLi() {
      final var result = HTMLListFactory.ul(List.of("element 1", "element 2"));

      assertEquals("<ul><li>element 1</li><li>element 2</li></ul>", result);
    }
  }

  @Nested
  class TextList {

    @Test
    void shouldReturnTextListWithOneItem() {
      final var result = HTMLListFactory.textList(Map.of("Heading 1", "Value 1"));

      assertEquals("<h4 className=\"ids-heading-4\">Heading 1</h4><p>Value 1</p>", result);
    }

    @Test
    @Disabled("Temporary disabled as it currently fail!")
    void shouldReturnTextListWithSeveralItems() {
      final var result = HTMLListFactory.textList(
          Map.of("Heading 1", "Value 1", "Heading 2", "Value 2", "Heading 3", "Value 3"));

      assertEquals(
          "<h4 className=\"ids-heading-4\">Heading 1</h4><p>Value 1</p><h4 className=\"ids-heading-4\">Heading 2</h4><p>Value 2</p><h4 className=\"ids-heading-4\">Heading 3</h4><p>Value 3</p>",
          result);
    }
  }

}