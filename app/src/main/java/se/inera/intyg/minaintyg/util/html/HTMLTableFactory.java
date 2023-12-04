package se.inera.intyg.minaintyg.util.html;


import static se.inera.intyg.minaintyg.util.html.HTMLFactory.tag;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.TableElement;

public class HTMLTableFactory {

  private HTMLTableFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static String table(List<List<TableElement>> elements) {
    return table(elements, Collections.emptyList(), HTMLTableFactory::tableElement);
  }

  public static String table(List<List<String>> elements, List<String> headings) {
    return table(elements, headings, HTMLTableFactory::td);
  }

  private static <T> String table(List<List<T>> values, List<String> headings,
      Function<T, String> elementMapper) {
    final var headingsContent = HTMLUtility.fromList(headings, HTMLTableFactory::th);
    final var tableBody = tbody(values, elementMapper);

    final var tableHeading = headingsContent.isEmpty() ? "" : tag("thead", headingsContent);
    final var tableContent = HTMLUtility.join(tableHeading, tableBody);

    return tag("table", "ids-table", tableContent);
  }

  private static String td(String value) {
    return tag("td", value);
  }

  private static String th(String value) {
    return tag("th", value);
  }

  private static String tr(String value) {
    return tag("tr", value);
  }

  private static <T> String tbody(List<List<T>> values, Function<T, String> mapper) {
    final var tbody = HTMLUtility.fromList(
        values,
        value -> tr(HTMLUtility.fromList(value, mapper))
    );

    return tag("tbody", tbody);
  }

  private static String tableElement(TableElement element) {
    return switch (element.getType()) {
      case DATA -> td(element.getValue());
      case HEADING -> th(element.getValue());
    };
  }
}
