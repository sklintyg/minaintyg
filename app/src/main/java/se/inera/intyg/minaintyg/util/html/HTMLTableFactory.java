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

import static se.inera.intyg.minaintyg.util.html.HTMLFactory.tag;
import static se.inera.intyg.minaintyg.util.html.HTMLFactory.tagWithChildren;

import java.util.List;
import java.util.function.Function;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.TableElement;

public class HTMLTableFactory {

  private HTMLTableFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static String generalTable(List<List<TableElement>> values, List<TableElement> headings) {
    final var headingsContent = tr(HTMLUtility.fromList(headings, HTMLTableFactory::tableElement));
    final var tableBody = tbody(values, HTMLTableFactory::tableElement);

    return table(tableBody, headingsContent);
  }

  public static String table(List<List<String>> values, List<String> headings) {
    final var headingsContent = HTMLUtility.fromList(headings, HTMLTableFactory::th);
    final var tableBody = tbody(values, HTMLTableFactory::td);

    return table(tableBody, headingsContent);
  }

  private static String table(String tbody, String thead) {
    final var tableHeading = thead.isEmpty() ? "" : tagWithChildren("thead", thead);
    final var tableContent = HTMLUtility.join(tableHeading, tbody);

    return tagWithChildren("table", "ids-table", tableContent);
  }

  private static String td(String value) {
    return tag("td", value);
  }

  private static String th(String value) {
    return tagWithChildren("th", value);
  }

  private static String tr(String value) {
    return tagWithChildren("tr", value);
  }

  private static <T> String tbody(List<List<T>> values, Function<T, String> mapper) {
    final var tbody =
        HTMLUtility.fromList(values, value -> tr(HTMLUtility.fromList(value, mapper)));

    return tagWithChildren("tbody", tbody);
  }

  private static String tableElement(TableElement element) {
    return switch (element.getType()) {
      case DATA -> td(element.getValue());
      case HEADING -> th(element.getValue());
    };
  }
}
