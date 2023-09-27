package se.inera.intyg.minaintyg.util.html;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class HTMLUtility {

  public static String join(String... s) {
    return Stream.of(s)
        .filter(Objects::nonNull)
        .collect(Collectors.joining());
  }

  public static <T> String fromList(List<T> list, Function<T, String> mapper) {
    return list
        .stream()
        .map(mapper)
        .collect(Collectors.joining());
  }
}
