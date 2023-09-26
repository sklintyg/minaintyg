package se.inera.intyg.minaintyg.util.html;


import static se.inera.intyg.minaintyg.util.html.HTMLFactory.tag;

import java.util.List;
import java.util.Map;

public class HTMLTableFactory {

  public static String table(Map<Integer, List<String>> values, List<String> headings) {
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

  private static String tbody(Map<Integer, List<String>> values) {
    final var tbody = HTMLUtility.fromMap(
        values,
        entry -> tr(HTMLUtility.fromList(entry.getValue(), HTMLTableFactory::td))
    );

    return tag("tbody", tbody);
  }
}
