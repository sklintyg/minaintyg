package se.inera.intyg.minaintyg.util.html;

import static org.springframework.web.util.HtmlUtils.htmlEscape;

import java.util.Map;
import java.util.stream.Collectors;

public class HTMLFactory {

  private static final String START_FIRST_TAG = "<";
  private static final String START_SECOND_TAG = ">";
  private static final String END_FIRST_TAG = "</";
  private static final String END_SECOND_TAG = ">";
  private static final String CLASSNAME = "className";
  private static final String START_ATTRIBUTE_TAG = "=\"";
  private static final String END_ATTRIBUTE_TAG = "\"";
  private static final String SPACE = " ";
  private static final String LINE_SEPARATOR = "\n";
  private static final String BR_TAG = "<br/>";

  private HTMLFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static String tag(String tagName, String className, String value, boolean isParent) {
    return tag(tagName, className, value, null, isParent);
  }

  public static String tag(String tagName, String className, String value) {
    return tag(tagName, className, value, null, false);
  }

  public static String tag(String tagName, String className, String value,
      Map<String, String> attributes) {
    return tag(tagName, className, value, attributes, false);
  }

  public static String tag(String tagName, String className, String value,
      Map<String, String> attributes, boolean isParent) {
    if (value == null || tagName == null || tagName.isEmpty()) {
      return "";
    }

    final var formattedAttributes = formatAttributes(attributes);
    final var text = formatText(value, isParent);

    return startTag(tagName, className, formattedAttributes) + text + endTag(tagName);
  }

  public static String tag(String tagName, String value) {
    return tag(tagName, null, value, null, false);
  }

  public static String tag(String tagName, String value, boolean isParent) {
    return tag(tagName, null, value, null, isParent);
  }

  private static String startTag(String tagName, String className, String attributes) {
    final var classNameTag = buildTag(CLASSNAME, className);

    return START_FIRST_TAG + tagName
        + classNameTag
        + attributes
        + START_SECOND_TAG;

  }

  private static String buildTag(String name, String value) {
    return name == null || value == null ? "" :
        SPACE + name + START_ATTRIBUTE_TAG + value + END_ATTRIBUTE_TAG;
  }

  private static String endTag(String tagName) {
    return END_FIRST_TAG + tagName + END_SECOND_TAG;
  }

  private static String formatText(String value, boolean isParent) {
    final var convertedText = isParent ? value : htmlEscape(value);
    return convertLineSeparators(convertedText);
  }

  private static String convertLineSeparators(String value) {
    return value.replace(LINE_SEPARATOR, BR_TAG);
  }

  private static String formatAttributes(Map<String, String> attributes) {
    if (attributes == null) {
      return "";
    }

    return attributes.entrySet().stream()
        .map(entry -> buildTag(entry.getKey(), entry.getValue()))
        .collect(Collectors.joining());
  }
}
