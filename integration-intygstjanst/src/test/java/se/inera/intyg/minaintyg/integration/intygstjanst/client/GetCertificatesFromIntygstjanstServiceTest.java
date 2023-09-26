package se.inera.intyg.minaintyg.integration.intygstjanst.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateListIntegrationRequest;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificatesResponseDTO;

@ExtendWith(MockitoExtension.class)
class GetCertificatesFromIntygstjanstServiceTest {

  private static MockWebServer mockWebServer;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private GetCertificatesFromIntygstjanstService getCertificatesFromIntygstjanstService;

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
    getCertificatesFromIntygstjanstService = new GetCertificatesFromIntygstjanstService(
        WebClient.create(baseUrl), scheme, baseUrl,
        mockWebServer.getPort(), endpoint);
  }

  @Test
  void shouldReturnCertificatesResponse() throws JsonProcessingException {
    final var request = GetCertificateListIntegrationRequest.builder().build();
    final var expectedResponse = CertificatesResponseDTO
        .builder()
        .content(List.of(CertificateDTO
            .builder()
            .build())
        )
        .build();

    mockWebServer.enqueue(
        new MockResponse().setBody(objectMapper.writeValueAsString(expectedResponse))
            .addHeader("Content-Type", "application/json"));

    final var actualResponse = getCertificatesFromIntygstjanstService.get(request);
    Assertions.assertEquals(expectedResponse, actualResponse);
  }
}