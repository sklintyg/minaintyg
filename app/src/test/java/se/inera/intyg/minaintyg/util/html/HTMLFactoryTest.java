package se.inera.intyg.minaintyg.util.html;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HTMLFactoryTest {

  @Test
  void shouldReturnArticle() {
    final var result = HTMLFactory.article("Test");

    assertEquals("<article className=\"ids-certificate\">Test</article>", result);
  }

  @Test
  void shouldReturnSection() {
    final var result = HTMLFactory.section("Test");

    assertEquals("<section>Test</section>", result);
  }

  @Test
  void shouldReturnCorrectTag() {
    final var result = HTMLFactory.tag("tag", "Value");

    assertEquals("<tag>Value</tag>", result);
  }

  @Test
  void shouldReturnEmptyStringIfTagIsNull() {
    final var result = HTMLFactory.tag(null, "Value");

    assertEquals("", result);
  }

  @Test
  void shouldReturnEmptyStringIfTagIsEmpty() {
    final var result = HTMLFactory.tag("", "Value");

    assertEquals("", result);
  }

  @Test
  void shouldReturnEmptyStringIfValueIsNull() {
    final var result = HTMLFactory.tag("", null);

    assertEquals("", result);
  }

  @Nested
  class WithClassName {

    @Test
    void shouldReturnCorrectTag() {
      final var result = HTMLFactory.tag("tag", "class", "Value");

      assertEquals("<tag className=\"class\">Value</tag>", result);
    }

    @Test
    void shouldReturnEmptyStringIfTagIsNull() {
      final var result = HTMLFactory.tag(null, "className", "Value");

      assertEquals("", result);
    }

    @Test
    void shouldReturnEmptyStringIfTagIsEmpty() {
      final var result = HTMLFactory.tag("", "className", "Value");

      assertEquals("", result);
    }

    @Test
    void shouldReturnEmptyStringIfValueIsNull() {
      final var result = HTMLFactory.tag("", "className", null);

      assertEquals("", result);
    }
  }
}