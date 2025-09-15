package se.inera.intyg.minaintyg.information.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

class Elva77LinkLoaderTest {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Elva77LinkLoader loader = new Elva77LinkLoader();
  public static final Resource LOCATION = new ClassPathResource(
      "links/1177-navbar-services-dummy.json");

  @Test
  void shouldLoadElva77MenuConfig() throws IOException {
    final var menuConfig = loader.load(LOCATION, objectMapper);

    assertNotNull(menuConfig);
  }

  @Test
  void shouldLoadCorrectVersion() throws IOException {
    final var menuConfig = loader.load(LOCATION, objectMapper);

    assertEquals("1.0.0-test", menuConfig.getVersion());
  }

  @Test
  void shouldLoadCorrectNumberOfLinks() throws IOException {
    final var menuConfig = loader.load(LOCATION, objectMapper);

    assertEquals(3, menuConfig.getMenu().getItems().size());
  }

  @Test
  void shouldLoadCorrectLinkName() throws IOException {
    final var menuConfig = loader.load(LOCATION, objectMapper);

    var firstLink = menuConfig.getMenu().getItems().getFirst();
    assertEquals("Test Start", firstLink.getName());
    assertEquals("https://prod.example.com/start", firstLink.getUrl().get("prod"));
    assertEquals("https://sys.example.com/start", firstLink.getUrl().get("sys"));
    assertEquals("https://acc.example.com/start", firstLink.getUrl().get("acc"));
  }

  @ParameterizedTest
  @MethodSource("environmentUrlProvider")
  void shouldLoadCorrectUrlForEnvironment(String environment, String expectedUrl)
      throws IOException {
    final var menuConfig = loader.load(LOCATION, objectMapper);

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
