package se.inera.intyg.minaintyg.util.html;

import static se.inera.intyg.minaintyg.util.html.HTMLFactory.tag;

import java.util.List;
import java.util.Map;

public class HTMLListFactory {

  public static String ul(List<String> values) {
    final var content = HTMLUtility.fromList(values, HTMLListFactory::li);

    return tag("ul", content);
  }

  public static String textList(Map<String, String> values) {
    return HTMLUtility.fromMap(
        values,
        value -> textListItem(value.getValue(), value.getKey())
    );
  }

  private static String textListItem(String value, String title) {
    return HTMLUtility.join(HTMLTextFactory.h4(title), HTMLTextFactory.p(value));
  }

  private static String li(String value) {
    return tag("li", value);
  }
}
