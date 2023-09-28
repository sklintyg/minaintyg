package se.inera.intyg.minaintyg.util.html;

import static se.inera.intyg.minaintyg.util.html.HTMLFactory.tag;

import java.util.List;

public class HTMLListFactory {

  private HTMLListFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static String ul(List<String> values) {
    final var content = HTMLUtility.fromList(values, HTMLListFactory::li);

    return tag("ul", content);
  }

  private static String li(String value) {
    return tag("li", value);
  }
}
