package se.inera.intyg.minaintyg.util.html;

import static se.inera.intyg.minaintyg.util.html.HTMLFactory.tag;

public class HTMLTextFactory {

  private static final String IDS_HEADING_2 = "ids-heading-2";
  private static final String IDS_HEADING_3 = "ids-heading-3";
  private static final String IDS_HEADING_4 = "ids-heading-4";
  private static final String IDS_HEADING_5 = "ids-heading-5";

  private HTMLTextFactory() {
    throw new IllegalStateException("Utility class");
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

  public static String h5(String value) {
    return tag("h5", IDS_HEADING_5, value);
  }

  public static String p(String value) {
    return tag("p", value);
  }

  public static String link(String url, String name) {
    return tag("IDSLink", null, name, "href", url);
  }

}
