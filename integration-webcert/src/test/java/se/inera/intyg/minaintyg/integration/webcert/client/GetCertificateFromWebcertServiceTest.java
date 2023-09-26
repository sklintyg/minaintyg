package se.inera.intyg.minaintyg.integration.webcert.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadata;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateResponseDTO;

class GetCertificateFromWebcertServiceTest {

  private static MockWebServer mockWebServer;
  private GetCertificateFromWebcertService getCertificateFromWebcertService;

  private static final String CERTIFICATE_ID = "certificateId";
  private static final String ID = "id";
  private final ObjectMapper objectMapper = new ObjectMapper();

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
    final var getPersonEndpoint = "/internalapi/certificate/{certificateId}";
    getCertificateFromWebcertService = new GetCertificateFromWebcertService(
        WebClient.create(baseUrl), scheme, baseUrl,
        mockWebServer.getPort(), getPersonEndpoint);
  }

  @Test
  void shouldReturnGetCertificateResponse() throws JsonProcessingException {
    final var getCertificateRequest = GetCertificateIntegrationRequest.builder()
        .certificateId(CERTIFICATE_ID)
        .build();
    final var expectedResponse = CertificateResponseDTO.builder()
        .certificate(CertificateDTO.builder()
            .data(Map.of(ID, CertificateDataElement.builder().build()))
            .metadata(CertificateMetadata.builder().build())
            .build())
        .build();
    mockWebServer.enqueue(
        new MockResponse().setBody(objectMapper.writeValueAsString(expectedResponse))
            .addHeader("Content-Type", "application/json"));
    final var actualResponse = getCertificateFromWebcertService.get(
        getCertificateRequest);
    assertEquals(expectedResponse, actualResponse);
  }
}
