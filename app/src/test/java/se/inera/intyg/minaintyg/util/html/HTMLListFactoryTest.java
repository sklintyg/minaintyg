package se.inera.intyg.minaintyg.util.html;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class HTMLListFactoryTest {

  @Test
  void shouldReturnUlWithOneLi() {
    final var result = HTMLListFactory.ul(List.of("element 1"));

    assertEquals("<ul><li>element 1</li></ul>", result);
  }

  @Test
  void shouldReturnUlWithTwoLi() {
    final var result = HTMLListFactory.ul(List.of("element 1", "element 2"));

    assertEquals("<ul><li>element 1</li><li>element 2</li></ul>", result);
  }
}