package se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
import se.inera.intyg.minaintyg.integration.api.citizen.GetCitizenIntegrationRequest;
import se.inera.intyg.minaintyg.integration.intygproxyservice.person.client.StatusDTO;

@ExtendWith(MockitoExtension.class)
class GetCitizenFromIntygProxyServiceImplTest {

  private static MockWebServer mockWebServer;
  private GetCitizenFromIntygProxyServiceImpl getCitizenFromIntygProxyService;

  private static final String PERSON_ID = "191212121212";
  private static final String PERSON_FIRSTNAME = "Arnold";
  private static final String PERSON_LASTNAME = "Johansson";
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
    final var getCitizenEndpoint = "/api/v1/citizen";
    getCitizenFromIntygProxyService = new GetCitizenFromIntygProxyServiceImpl(
        WebClient.create(baseUrl), scheme, baseUrl,
        mockWebServer.getPort(), getCitizenEndpoint);
  }

  @Test
  void shouldReturnPersonResponse() throws JsonProcessingException {
    final var citizenRequest = GetCitizenIntegrationRequest.builder().personId(PERSON_ID).build();
    final var expectedResponse = CitizenResponseDTO.builder()
        .citizen(
            CitizenDTO.builder()
                .personnummer(PERSON_ID)
                .fornamn(PERSON_FIRSTNAME)
                .efternamn(PERSON_LASTNAME)
                .build()
        )
        .status(StatusDTO.FOUND)
        .build();
    mockWebServer.enqueue(
        new MockResponse().setBody(objectMapper.writeValueAsString(expectedResponse))
            .addHeader("Content-Type", "application/json"));
    final var actualResponse = getCitizenFromIntygProxyService.getCitizenFromIntygProxy(
        citizenRequest);
    Assertions.assertEquals(expectedResponse, actualResponse);
  }

}