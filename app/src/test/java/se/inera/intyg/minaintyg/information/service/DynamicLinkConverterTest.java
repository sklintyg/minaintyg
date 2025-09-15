package se.inera.intyg.minaintyg.information.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.information.service.model.DynamicLink;

class DynamicLinkConverterTest {

  public static final DynamicLink LINK = DynamicLink.builder()
      .id("1")
      .name("test")
      .url(Collections.emptyMap())
      .isAgentModeSupported(false)
      .build();

  @Test
  void shouldSetID() {
    final var response = new DynamicLinkConverter().convert(LINK, "prod");
    assertEquals(LINK.getId(), response.getId());
  }

  @Test
  void shouldSetName() {
    final var response = new DynamicLinkConverter().convert(LINK, "prod");
    assertEquals(LINK.getName(), response.getName());
  }

  @Test
  void shouldSetUrl() {
    final var response = new DynamicLinkConverter().convert(LINK, "prod");
    assertEquals(LINK.getUrl().get("prod"), response.getUrl());
  }
}
