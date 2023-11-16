package se.inera.intyg.minaintyg.util.html;

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

  public static String tag(String tagName, String className, String value) {
    return tag(tagName, className, value, null, null);
  }

  public static String tag(String tagName, String className, String value, String attributeName,
      String attributeValue) {
    if (value == null || tagName == null || tagName.isEmpty()) {
      return "";
    }
    final var text = convertLineSeparatorsIfPresent(value);
    return startTag(tagName, className, attributeName, attributeValue) + text + endTag(tagName);
  }

  public static String tag(String tagName, String value) {
    return tag(tagName, null, value, null, null);
  }

  private static String startTag(String tagName, String className, String attributeName,
      String attributeValue) {
    
    final var attribute = buildTag(attributeName, attributeValue);
    final var classNameTag = buildTag(CLASSNAME, className);

    return START_FIRST_TAG + tagName
        + classNameTag
        + attribute
        + START_SECOND_TAG;

  }

  private static String buildTag(String name, String value) {
    return name == null || value == null ? "" :
        SPACE + name + START_ATTRIBUTE_TAG + value + END_ATTRIBUTE_TAG;
  }

  private static String endTag(String tagName) {
    return END_FIRST_TAG + tagName + END_SECOND_TAG;
  }

  private static String convertLineSeparatorsIfPresent(String value) {
    return convertLineSeparators(value);
  }

  private static String convertLineSeparators(String value) {
    return value.replace(LINE_SEPARATOR, BR_TAG);
  }
}
