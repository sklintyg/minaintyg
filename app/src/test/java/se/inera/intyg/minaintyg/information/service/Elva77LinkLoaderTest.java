/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.minaintyg.information.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
  public static final Resource LOCATION =
      new ClassPathResource("links/1177-navbar-services-dummy.json");

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

    final var firstLink = menuConfig.getMenu().getItems().getFirst();
    assertEquals("https://prod.example.com/start", firstLink.getUrl().get("prod"));
  }

  @Test
  void shouldThrowExceptionForInvalidResource() {
    final var invalidResource = new ClassPathResource("links/non-existing-file.json");

    assertThrows(IllegalStateException.class, () -> loader.load(invalidResource));
  }

  @ParameterizedTest
  @MethodSource("environmentUrlProvider")
  void shouldLoadCorrectUrlForEnvironment(String environment, String expectedUrl) {
    final var menuConfig = loader.load(LOCATION);

    final var firstLink = menuConfig.getMenu().getItems().getFirst();

    final var actualUrl = firstLink.getUrl().get(environment);
    assertEquals(expectedUrl, actualUrl);
  }

  private static Stream<Arguments> environmentUrlProvider() {
    return Stream.of(
        Arguments.of("prod", "https://prod.example.com/start"),
        Arguments.of("acc", "https://acc.example.com/start"),
        Arguments.of("sys", "https://sys.example.com/start"));
  }
}
