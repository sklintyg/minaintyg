package se.inera.intyg.minaintyg.util;

import java.util.List;
import java.util.Map;

public class HTMLFactory {

  // TODO: Create new factory for table?

  private static final String START_FIRST_TAG = "<";
  private static final String START_SECOND_TAG = ">";
  private static final String END_FIRST_TAG = "</";
  private static final String END_SECOND_TAG = ">";
  private static final String CLASSNAME = "className";
  private static final String START_ATTRIBUTE_TAG = "=\"";
  private static final String END_ATTRIBUTE_TAG = "\"";
  private static final String IDS_HEADING_2 = "ids-heading-2";
  private static final String IDS_HEADING_3 = "ids-heading-3";
  private static final String IDS_HEADING_4 = "ids-heading-4";
  private static final String IDS_CERTIFICATE = "ids-certificate";
  private static final String SPACE = " ";

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
    final var content = HTMLUtility.fromList(values, HTMLFactory::li);

    return tag("ul", content);
  }

  public static String textList(Map<String, String> values) {
    return HTMLUtility.fromMap(
        values,
        value -> textListItem(value.getValue(), value.getKey())
    );
  }

  private static String textListItem(String value, String title) {
    return HTMLUtility.join(HTMLFactory.h4(title), HTMLFactory.p(value));
  }

  private static String li(String value) {
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
    final var tbody = HTMLUtility.fromMap(
        values,
        entry -> tr(HTMLUtility.fromList(entry.getValue(), HTMLFactory::td))
    );

    return tag("tbody", tbody);
  }

  private static String tag(String tagName, String className, String value) {
    return startTag(tagName, className) + value + endTag(tagName);
  }

  private static String tag(String tagName, String value) {
    return startTag(tagName, null) + value + endTag(tagName);
  }

  private static String startTag(String tagName, String className) {
    if (className != null) {
      return START_FIRST_TAG + tagName + SPACE
          + CLASSNAME + START_ATTRIBUTE_TAG + className + END_ATTRIBUTE_TAG
          + START_SECOND_TAG;
    }

    return START_FIRST_TAG + tagName + START_SECOND_TAG;
  }

  private static String endTag(String tagName) {
    return END_FIRST_TAG + tagName + END_SECOND_TAG;
  }

  public static String table(Map<Integer, List<String>> values, List<String> headings) {
    final var headingsContent = HTMLUtility.fromList(headings, HTMLFactory::th);
    final var tableBody = tbody(values);

    final var tableHeading = tag("thead", headingsContent);
    final var tableContent = HTMLUtility.join(tableHeading, tableBody);

    return tag("table", "ids-table", tableContent);
  }
}
