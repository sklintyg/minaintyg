/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
    final var result = HTMLFactory.tag("p", "Value<Value");

    assertEquals("<p>Value&lt;Value</p>", result);
  }

  @Test
  void shouldNotConvertSpecialCharactersIfParent() {
    final var result = HTMLFactory.tagWithChildren("p", "Value<p>Value</p> test");

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

      assertEquals(
          "<tag className=\"class\" attribute=\"attributeValue\" href=\"url\">Value</tag>", result);
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
      final var result = HTMLFactory.tag("p", "class", "Value<Value");

      assertEquals("<p className=\"class\">Value&lt;Value</p>", result);
    }

    @Test
    void shouldNotConvertSpecialCharactersIfParent() {
      final var result = HTMLFactory.tagWithChildren("p", "class", "Value<p>Value</p> test");

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
