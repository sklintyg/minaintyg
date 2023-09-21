package se.inera.intyg.minaintyg.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

// TODO: Switch map to linkedmap so they preserve order

@ExtendWith(MockitoExtension.class)
class HTMLFactoryTest {

  @Test
  void shouldReturnArticle() {
    final var result = HTMLFactory.article(HTMLFactory.p("Test"));

    assertEquals("<article className=\"ids-certificate\"><p>Test</p></article>", result);
  }

  @Test
  void shouldReturnSection() {
    final var result = HTMLFactory.section(HTMLFactory.p("Test"));

    assertEquals("<section><p>Test</p></section>", result);
  }

  @Nested
  class Text {

    @Test
    void shouldReturnP() {
      final var result = HTMLFactory.p("title");

      assertEquals("<p>title</p>", result);
    }

    @Nested
    class Headings {

      @Test
      void shouldReturnH2() {
        final var result = HTMLFactory.h2("title");

        assertEquals("<h2 className=\"ids-heading-2\">title</h2>", result);
      }

      @Test
      void shouldReturnH3() {
        final var result = HTMLFactory.h3("title");

        assertEquals("<h3 className=\"ids-heading-3\">title</h3>", result);
      }

      @Test
      void shouldReturnH4() {
        final var result = HTMLFactory.h4("title");

        assertEquals("<h4 className=\"ids-heading-4\">title</h4>", result);
      }
    }
  }

  @Nested
  class TestList {

    @Test
    void shouldReturnUlWithOneLi() {
      final var result = HTMLFactory.ul(List.of("element 1"));

      assertEquals("<ul><li>element 1</li></ul>", result);
    }

    @Test
    void shouldReturnUlWithTwoLi() {
      final var result = HTMLFactory.ul(List.of("element 1", "element 2"));

      assertEquals("<ul><li>element 1</li><li>element 2</li></ul>", result);
    }
  }

  @Nested
  class Table {

    @Test
    void shouldReturnTableWithOneTr() {
      final var result = HTMLFactory.table(
          Map.of(1, List.of("Value 1", "Value 2")),
          List.of("heading 1", "heading 2")
      );

      assertEquals(
          "<table className=\"ids-table\"><thead><th>heading 1</th><th>heading 2</th></thead><tbody><tr><td>Value 1</td><td>Value 2</td></tr></tbody></table>",
          result);
    }

    @Test
    void shouldReturnUlWithTwoTr() {
      final var result = HTMLFactory.table(
          Map.of(1, List.of("Value 1", "Value 2"), 2, List.of("Value 3", "Value 4")),
          List.of("heading 1", "heading 2")
      );

      assertEquals(
          "<table className=\"ids-table\"><thead><th>heading 1</th><th>heading 2</th></thead><tbody><tr><td>Value 1</td><td>Value 2</td></tr><tr><td>Value 3</td><td>Value 4</td></tr></tbody></table>",
          result);
    }
  }

  @Nested
  class TextList {

    @Test
    void shouldReturnTextListWithOneItem() {
      final var result = HTMLFactory.textList(Map.of("Heading 1", "Value 1"));

      assertEquals("<h4 className=\"ids-heading-4\">Heading 1</h4><p>Value 1</p>", result);
    }

    @Test
    void shouldReturnTextListWithSeveralItems() {
      final var result = HTMLFactory.textList(
          Map.of("Heading 1", "Value 1", "Heading 2", "Value 2", "Heading 3", "Value 3"));

      assertEquals(
          "<h4 className=\"ids-heading-4\">Heading 1</h4><p>Value 1</p><h4 className=\"ids-heading-4\">Heading 2</h4><p>Value 2</p><h4 className=\"ids-heading-4\">Heading 3</h4><p>Value 3</p>",
          result);
    }
  }
}