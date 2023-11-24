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
    getBannersFromIntygsadminService = new GetBannersFromIntygsadminService(
        WebClient.create(baseUrl), scheme, baseUrl,
        mockWebServer.getPort(), endpoint);
  }

  @Test
  void shouldReturnBannersDTO() throws JsonProcessingException {
    final var expectedResponse = List.of(BannerDTO.builder().build());
    mockWebServer.enqueue(
        new MockResponse().setBody(objectMapper.writeValueAsString(expectedResponse))
            .addHeader("Content-Type", "application/json"));

    final var actualResponse = getBannersFromIntygsadminService.get();
    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  void shouldReturnEmptyList() throws JsonProcessingException {
    mockWebServer.enqueue(
        new MockResponse().setBody(objectMapper.writeValueAsString(null))
            .addHeader("Content-Type", "application/json"));

    final var actualResponse = getBannersFromIntygsadminService.get();
    assertTrue(actualResponse.isEmpty());
  }
}
