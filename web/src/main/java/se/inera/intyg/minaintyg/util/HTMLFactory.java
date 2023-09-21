package se.inera.intyg.minaintyg.util;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HTMLFactory {

  // TODO: Create new factory for table?

  private static final String START_FIRST_TAG = "<";
  private static final String START_SECOND_TAG = ">";
  private static final String END_FIRST_TAG = "<";
  private static final String END_SECOND_TAG = "/>";
  private static final String CLASSNAME = "className";
  private static final String START_ATTRIBUTE_TAG = "={";
  private static final String END_ATTRIBUTE_TAG = "}";
  private static final String IDS_HEADING_2 = "ids-heading-2";
  private static final String IDS_HEADING_3 = "ids-heading-3";
  private static final String IDS_HEADING_4 = "ids-heading-4";
  private static final String IDS_CERTIFICATE = "ids-certificate";


  public static String tag(String tagName, String className, String value) {
    return START_FIRST_TAG + tagName
        + CLASSNAME + START_ATTRIBUTE_TAG + className + END_ATTRIBUTE_TAG
        + START_SECOND_TAG + value + END_FIRST_TAG + END_SECOND_TAG;
  }

  public static String tag(String tagName, String value) {
    return START_FIRST_TAG + tagName + START_SECOND_TAG
        + value + END_FIRST_TAG + END_SECOND_TAG;
  }

  public static String tag(String tagName) {
    return START_FIRST_TAG + tagName + END_SECOND_TAG;
  }

  public static String section(String value) {
    return tag("section", value);
  }

  public static String article(String value) {
    return tag("article", IDS_CERTIFICATE, value);
  }

  public static String h2(String value) {
    return tag("h2", IDS_HEADING_2, value);
  }

  public static String h3(String value) {
    return tag("h3", IDS_HEADING_3, value);
  }

  public static String h4(String value) {
    return tag("h4", IDS_HEADING_4, value);
  }

  public static String p(String value) {
    return tag("p", value);
  }

  public static String ul(List<String> values) {
    final var content = getValues(values, HTMLFactory::li);

    return tag("ul", content);
  }

  public static String li(String value) {
    return tag("li", value);
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
    final var tbody = values
        .values()
        .stream()
        .map(strings ->
            tr(
                getValues(strings, HTMLFactory::td)
            )
        )
        .collect(Collectors.joining());

    return tag("tbody", tbody);
  }

  public static String table(Map<Integer, List<String>> values, List<String> headings) {
    final var headingsContent = getValues(headings, HTMLFactory::th);
    final var tableBody = tbody(values);

    final var tableHeading = tag("thead", headingsContent);
    final var tableContent = HTMLUtility.join(tableHeading, tableBody);

    return tag("table", "ids-table", tableContent);
  }

  private static String getValues(List<String> values, Function<String, String> valueFunction) {
    return values
        .stream()
        .map(valueFunction)
        .collect(Collectors.joining());
  }

}
