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
package se.inera.intyg.minaintyg.integration.intygsadmin.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;

@ExtendWith(MockitoExtension.class)
class GetBannersFromIntygsadminServiceTest {

  private static MockWebServer mockWebServer;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private GetBannersFromIntygsadminService getBannersFromIntygsadminService;

  @BeforeAll
  static void setUp() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
  }

  @AfterAll
  static void tearDown() throws IOException {
    mockWebServer.shutdown();
  }

  @BeforeEach
  void initialize() {
    final var scheme = "http";
    final var baseUrl = "localhost";
    final var endpoint = "/api/certificate";
    getBannersFromIntygsadminService =
        new GetBannersFromIntygsadminService(
            WebClient.create(baseUrl), scheme, baseUrl, mockWebServer.getPort(), endpoint);
  }

  @Test
  void shouldReturnBannersDTO() throws JsonProcessingException {
    final var expectedResponse = List.of(BannerDTO.builder().build());
    mockWebServer.enqueue(
        new MockResponse()
            .setBody(objectMapper.writeValueAsString(expectedResponse))
            .addHeader("Content-Type", "application/json"));

    final var actualResponse = getBannersFromIntygsadminService.get();
    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  void shouldReturnEmptyList() throws JsonProcessingException {
    mockWebServer.enqueue(
        new MockResponse()
            .setBody(objectMapper.writeValueAsString(null))
            .addHeader("Content-Type", "application/json"));

    final var actualResponse = getBannersFromIntygsadminService.get();
    assertTrue(actualResponse.isEmpty());
  }
}
