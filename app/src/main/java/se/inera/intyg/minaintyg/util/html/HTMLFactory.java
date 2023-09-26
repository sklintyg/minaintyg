package se.inera.intyg.minaintyg.util.html;

public class HTMLFactory {

  private static final String START_FIRST_TAG = "<";
  private static final String START_SECOND_TAG = ">";
  private static final String END_FIRST_TAG = "</";
  private static final String END_SECOND_TAG = ">";
  private static final String CLASSNAME = "className";
  private static final String START_ATTRIBUTE_TAG = "=\"";
  private static final String END_ATTRIBUTE_TAG = "\"";
  private static final String IDS_CERTIFICATE = "ids-certificate";
  private static final String SPACE = " ";

  public static String tag(String tagName, String className, String value) {
    return startTag(tagName, className) + value + endTag(tagName);
  }

  public static String tag(String tagName, String value) {
    return startTag(tagName, null) + value + endTag(tagName);
  }


  public static String section(String value) {
    return tag("section", value);
  }

  public static String article(String value) {
    return tag("article", IDS_CERTIFICATE, value);
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
