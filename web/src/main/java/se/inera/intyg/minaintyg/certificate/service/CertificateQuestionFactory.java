package se.inera.intyg.minaintyg.certificate.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import se.inera.intyg.minaintyg.util.HTMLUtility;

public class CertificateQuestionFactory {

  public static String textList(Map<String, String> values) {
    final var content = fromMap(values, value -> textListItem(value.getValue(), value.getKey()));

    return
  }

  public static String category(String value) {

  }

  private static <T, S> String fromMap(Map<T, S> values, Function<Entry<T, S>, String> mapper) {
    return values
        .entrySet()
        .stream()
        .map(mapper)
        .collect(Collectors.joining());
  }

  private static String textListItem(String value, String title) {
    return HTMLUtility.join(HTMLFactory.h4(title), HTMLFactory.p(value));
  }

}
