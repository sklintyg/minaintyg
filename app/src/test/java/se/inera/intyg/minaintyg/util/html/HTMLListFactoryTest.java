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

import java.util.List;
import org.junit.jupiter.api.Test;

class HTMLListFactoryTest {

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
