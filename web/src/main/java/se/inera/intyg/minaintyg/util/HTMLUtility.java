package se.inera.intyg.minaintyg.util;

import se.inera.intyg.minaintyg.certificate.service.HTMLFactory;

public final class HTMLUtility {

  public static String wrapWith(String tag, String content) {
    return HTMLFactory.tag(tag, content);
  }

  public static String join(String s1, String s2) {
    return s1.concat(s2);
  }

}
