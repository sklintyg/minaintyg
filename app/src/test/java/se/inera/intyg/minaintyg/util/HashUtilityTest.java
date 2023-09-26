package se.inera.intyg.minaintyg.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HashUtilityTest {

  @Test
  void shouldReturnHashedValue() {
    final var payload = "123123123";
    final var hashedPayload = HashUtility.hash(payload);
    assertEquals("932f3c1b56257ce8539ac269d7aab42550dacf8818d075f0bdf1990562aae3ef", hashedPayload);
  }

  @Test
  void shouldReturnEmptyHashConstantWhenPayloadIsNull() {
    final var hashedPayload = HashUtility.hash(null);
    assertEquals(HashUtility.EMPTY, hashedPayload);
  }

  @Test
  void shouldReturnEmptyHashConstantWhenPayloadIsEmpty() {
    final var payload = "";
    final var hashedPayload = HashUtility.hash(payload);
    assertEquals(HashUtility.EMPTY, hashedPayload);
  }
}