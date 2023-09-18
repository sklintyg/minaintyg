package se.inera.intyg.minaintyg.integration.intygproxyservice.person.client;

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
import se.inera.intyg.minaintyg.integration.api.person.PersonRequest;

@ExtendWith(MockitoExtension.class)
class GetPersonServiceImplTest {

  private static MockWebServer mockWebServer;
  private GetPersonFromIntygProxyServiceImpl getPersonFromIntygProxyService;

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
    final var getPersonEndpoint = "/api/v1/person";
    final var traceIdHeader = "tracerIdHeader";
    final var traceIdKey = "tracerIdHeader";
    getPersonFromIntygProxyService = new GetPersonFromIntygProxyServiceImpl(
        WebClient.create(baseUrl), scheme, baseUrl,
        mockWebServer.getPort(), getPersonEndpoint, traceIdHeader, traceIdKey);
  }

  @Test
  void shouldReturnPersonResponse() throws JsonProcessingException {
    final var personRequest = PersonRequest.builder().personId(PERSON_ID).build();
    final var expectedResponse = PersonSvarDTO.builder()
        .person(
            PersonDTO.builder()
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
    final var actualResponse = getPersonFromIntygProxyService.getPersonFromIntygProxy(
        personRequest);
    Assertions.assertEquals(expectedResponse, actualResponse);
  }
}
