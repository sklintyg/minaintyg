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
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.TableElement;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.TableElementType;

class HTMLTableFactoryTest {

  @Test
  void shouldReturnTableWithOneTr() {
    final var result =
        HTMLTableFactory.table(
            List.of(List.of("Value 1", "Value 2")), List.of("heading 1", "heading 2"));

    assertEquals(
        "<table className=\"ids-table\"><thead><th>heading 1</th><th>heading 2</th></thead><tbody><tr><td>Value 1</td><td>Value 2</td></tr></tbody></table>",
        result);
  }

  @Test
  void shouldReturnGeneralTable() {
    final var result =
        HTMLTableFactory.generalTable(
            List.of(
                List.of(TableElement.builder().type(TableElementType.DATA).value("data").build())),
            List.of(
                TableElement.builder().type(TableElementType.HEADING).value("heading").build()));

    assertEquals(
        "<table className=\"ids-table\"><thead><tr><th>heading</th></tr></thead><tbody><tr><td>data</td></tr></tbody></table>",
        result);
  }

  @Test
  void shouldReturnTableWithTwoTr() {
    final var result =
        HTMLTableFactory.table(
            List.of(List.of("Value 1", "Value 2"), List.of("Value 3", "Value 4")),
            List.of("heading 1", "heading 2"));

    assertEquals(
        "<table className=\"ids-table\"><thead><th>heading 1</th><th>heading 2</th></thead><tbody><tr><td>Value 1</td><td>Value 2</td></tr><tr><td>Value 3</td><td>Value 4</td></tr></tbody></table>",
        result);
  }
}
