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
  public static final String LINE_SEPARATOR = "\n";
  public static final String BR_TAG = "<br/>";

  private HTMLFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static String tag(String tagName, String className, String value) {
    if (value == null || tagName == null || tagName.isEmpty()) {
      return "";
    }
    final var text = convertLineSeparatorsIfPresent(value);
    return startTag(tagName, className) + text + endTag(tagName);
  }

  private static String convertLineSeparatorsIfPresent(String value) {
    if (hasLineSeparators(value)) {
      return convertLineSeparators(value);
    }
    return value;
  }

  private static String convertLineSeparators(String value) {
    return value.replace(LINE_SEPARATOR, BR_TAG);
  }

  private static boolean hasLineSeparators(String value) {
    return value.contains("\n");
  }

  public static String tag(String tagName, String value) {
    return tag(tagName, null, value);
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
}
