package se.inera.intyg.minaintyg.information.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.information.service.model.DynamicLink;

class Elva77MenuLinksConverterTest {

  public static final DynamicLink LINK = DynamicLink.builder()
      .id("1")
      .name("test")
      .url("https://example.com")
      .build();

  @Test
  void shouldSetID() {
    final var response = new DynamicLinkConverter().convert(LINK);
    assertEquals(LINK.getId(), response.getId());
  }

  @Test
  void shouldSetName() {
    final var response = new DynamicLinkConverter().convert(LINK);
    assertEquals(LINK.getName(), response.getName());
  }

  @Test
  void shouldSetUrl() {
    final var response = new DynamicLinkConverter().convert(LINK);
    assertEquals(LINK.getUrl(), response.getUrl());
  }
}
