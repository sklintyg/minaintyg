package se.inera.intyg.minaintyg.util.html;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

// TODO: Switch map to linkedmap so they preserve order

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
  void shouldReturnCorrectTagWithClassname() {
    final var result = HTMLFactory.tag("tag", "class", "Value");

    assertEquals("<tag className=\"class\">Value</tag>", result);
  }
}