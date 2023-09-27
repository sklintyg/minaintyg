package se.inera.intyg.minaintyg.util.html;


import static se.inera.intyg.minaintyg.util.html.HTMLFactory.tag;

import java.util.List;

public class HTMLTableFactory {

  private HTMLTableFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static String table(List<List<String>> values, List<String> headings) {
    final var headingsContent = HTMLUtility.fromList(headings, HTMLTableFactory::th);
    final var tableBody = tbody(values);

    final var tableHeading = tag("thead", headingsContent);
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

  private static String tbody(List<List<String>> values) {
    final var tbody = HTMLUtility.fromList(
        values,
        value -> tr(HTMLUtility.fromList(value, HTMLTableFactory::td))
    );

    return tag("tbody", tbody);
  }
}
