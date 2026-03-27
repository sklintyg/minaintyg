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

    assertEquals("<a href=\"url\" target=\"_blank\">value</a>", result);
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
