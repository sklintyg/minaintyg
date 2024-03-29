package se.inera.intyg.minaintyg.util.html;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class HTMLUtilityTest {

  private static final String S1 = "S_1";
  private static final String S2 = "S_2";
  private static final String S3 = "S_3";

  @Test
  void shouldJoinTwoTexts() {
    final var response = HTMLUtility.join(S1, S2);

    assertEquals(response, S1 + S2);
  }

  @Test
  void shouldJoinThreeTexts() {
    final var response = HTMLUtility.join(S1, S2, S3);

    assertEquals(response, S1 + S2 + S3);
  }

  @Test
  void shouldNotJoinNull() {
    final var response = HTMLUtility.join(S1, S2, S3, null);

    assertEquals(response, S1 + S2 + S3);
  }

  @Test
  void shouldFormatTextsInList() {
    final var response = HTMLUtility.fromList(List.of(S1, S2), HTMLTextFactory::p);

    assertEquals(response, "<p>" + S1 + "</p><p>" + S2 + "</p>");
  }

  @Test
  void shouldReturEmptyStringIfListIsEmpty() {
    final var response = HTMLUtility.fromList(Collections.emptyList(), HTMLTextFactory::p);

    assertEquals("", response);
  }

}