package se.inera.intyg.minaintyg.information.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Elva77LinkLoaderTest {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Elva77LinkLoader loader = new Elva77LinkLoader(objectMapper);
  public static final Resource LOCATION = new ClassPathResource(
      "links/1177-navbar-services-dummy.json");

  @Test
  void shouldLoadElva77MenuConfig() {
    final var menuConfig = loader.load(LOCATION);

    assertNotNull(menuConfig);
  }

  @Test
  void shouldLoadCorrectVersion() {
    final var menuConfig = loader.load(LOCATION);

    assertEquals("1.0.0-test", menuConfig.getVersion());
  }

  @Test
  void shouldLoadCorrectNumberOfLinks() {
    final var menuConfig = loader.load(LOCATION);

    assertEquals(3, menuConfig.getMenu().getItems().size());
  }

  @Test
  void shouldLoadCorrectLinkName() {
    final var menuConfig = loader.load(LOCATION);

    var firstLink = menuConfig.getMenu().getItems().getFirst();
    assertEquals("https://prod.example.com/start", firstLink.getUrl().get("prod"));
  }

  @Test
  void shouldThrowExceptionForInvalidResource() {
    final var invalidResource = new ClassPathResource("links/non-existing-file.json");

    try {
      loader.load(invalidResource);
    } catch (IllegalStateException e) {
      assertEquals(
          "Failed to load or parse resource: class path resource [links/non-existing-file.json]",
          e.getMessage());
    }
  }

  @ParameterizedTest
  @MethodSource("environmentUrlProvider")
  void shouldLoadCorrectUrlForEnvironment(String environment, String expectedUrl) {
    final var menuConfig = loader.load(LOCATION);

    var firstLink = menuConfig.getMenu().getItems().getFirst();

    String actualUrl = firstLink.getUrl().get(environment);
    assertEquals(expectedUrl, actualUrl);
  }


  private static Stream<Arguments> environmentUrlProvider() {
    return Stream.of(
        Arguments.of("prod", "https://prod.example.com/start"),
        Arguments.of("acc", "https://acc.example.com/start"),
        Arguments.of("sys", "https://sys.example.com/start")
    );
  }

}
