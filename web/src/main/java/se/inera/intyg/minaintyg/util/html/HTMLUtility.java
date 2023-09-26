package se.inera.intyg.minaintyg.util.html;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class HTMLUtility {

  public static String join(String... s) {
    return String.join("", s);
  }

  public static <T> String fromList(List<T> list, Function<T, String> mapper) {
    return list
        .stream()
        .map(mapper)
        .collect(Collectors.joining());
  }

  public static <T, S> String fromMap(Map<T, S> values, Function<Entry<T, S>, String> mapper) {
    return values
        .entrySet()
        .stream()
        .map(mapper)
        .collect(Collectors.joining());
  }
}
