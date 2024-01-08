package se.inera.intyg.minaintyg.util.html;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HTMLFactoryTest {

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

  @Test
  void shouldReturnCorrectTagWithAttribute() {
    final var attributes = new HashMap<String, String>();
    attributes.put("attribute", "attributeValue");
    attributes.put("href", "url");
    final var result = HTMLFactory.tag("tag", null, "Value", attributes);

    assertEquals("<tag attribute=\"attributeValue\" href=\"url\">Value</tag>", result);
  }

  @Test
  void shouldConvertLineSeparatorsToBr() {
    final var result = HTMLFactory.tag("tag", "Value\nValue");

    assertEquals("<tag>Value<br/>Value</tag>", result);
  }

  @Test
  void shouldConvertMultipleLineSeparatorsToBr() {
    final var result = HTMLFactory.tag("tag", "Value\nValue\nValue");

    assertEquals("<tag>Value<br/>Value<br/>Value</tag>", result);
  }

  @Test
  void shouldConvertSpecialCharactersAsDefault() {
    final var result = HTMLFactory.tag("p", "Value<Value");

    assertEquals("<p>Value&lt;Value</p>", result);
  }

  @Test
  void shouldConvertSpecialCharactersIfNotParent() {
    final var result = HTMLFactory.tag("p", "Value<Value", false);

    assertEquals("<p>Value&lt;Value</p>", result);
  }

  @Test
  void shouldNotConvertSpecialCharactersIfParent() {
    final var result = HTMLFactory.tag("p", "Value<p>Value</p> test", true);

    assertEquals("<p>Value<p>Value</p> test</p>", result);
  }

  @Nested
  class WithClassName {

    @Test
    void shouldReturnCorrectTag() {
      final var result = HTMLFactory.tag("tag", "class", "Value");

      assertEquals("<tag className=\"class\">Value</tag>", result);
    }

    @Test
    void shouldReturnCorrectTagWithAttributes() {
      final var attributes = new HashMap<String, String>();
      attributes.put("attribute", "attributeValue");
      attributes.put("href", "url");

      final var result = HTMLFactory.tag("tag", "class", "Value", attributes);

      assertEquals("<tag className=\"class\" attribute=\"attributeValue\" href=\"url\">Value</tag>",
          result);
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

    @Test
    void shouldConvertSpecialCharactersAsDefault() {
      final var result = HTMLFactory.tag("p", "class", "Value<Value");

      assertEquals("<p className=\"class\">Value&lt;Value</p>", result);
    }

    @Test
    void shouldConvertSpecialCharactersIfNotParent() {
      final var result = HTMLFactory.tag("p", "class", "Value<Value", false);

      assertEquals("<p className=\"class\">Value&lt;Value</p>", result);
    }

    @Test
    void shouldNotConvertSpecialCharactersIfParent() {
      final var result = HTMLFactory.tag("p", "class", "Value<p>Value</p> test", true);

      assertEquals("<p className=\"class\">Value<p>Value</p> test</p>", result);
    }

    @Test
    void shouldConvertLineSeparatorsToBr() {
      final var result = HTMLFactory.tag("tag", "class", "Value\nValue");

      assertEquals("<tag className=\"class\">Value<br/>Value</tag>", result);
    }

    @Test
    void shouldConvertMultipleLineSeparatorsToBr() {
      final var result = HTMLFactory.tag("tag", "class", "Value\nValue\nValue");

      assertEquals("<tag className=\"class\">Value<br/>Value<br/>Value</tag>", result);
    }
  }
}
