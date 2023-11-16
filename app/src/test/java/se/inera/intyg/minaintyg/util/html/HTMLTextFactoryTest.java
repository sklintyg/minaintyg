package se.inera.intyg.minaintyg.util.html;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HTMLTextFactoryTest {

  @Test
  void shouldReturnP() {
    final var result = HTMLTextFactory.p("title");

    assertEquals("<p>title</p>", result);
  }

  @Test
  void shouldReturnLink() {
    final var result = HTMLTextFactory.link("url", "value");

    assertEquals("<IDSLink href=\"url\">value</IDSLink>", result);
  }

  @Nested
  class Headings {

    @Test
    void shouldReturnH2() {
      final var result = HTMLTextFactory.h2("title");

      assertEquals("<h2 className=\"ids-heading-2\">title</h2>", result);
    }

    @Test
    void shouldReturnH3() {
      final var result = HTMLTextFactory.h3("title");

      assertEquals("<h3 className=\"ids-heading-3\">title</h3>", result);
    }

    @Test
    void shouldReturnH4() {
      final var result = HTMLTextFactory.h4("title");

      assertEquals("<h4 className=\"ids-heading-4\">title</h4>", result);
    }

    @Test
    void shouldReturnH5() {
      final var result = HTMLTextFactory.h5("title");

      assertEquals("<h5 className=\"ids-heading-5\">title</h5>", result);
    }
  }
}