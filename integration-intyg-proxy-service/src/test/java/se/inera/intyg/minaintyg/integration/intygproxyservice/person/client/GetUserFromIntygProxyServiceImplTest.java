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
import se.inera.intyg.minaintyg.integration.api.person.GetUserIntegrationRequest;

@ExtendWith(MockitoExtension.class)
class GetUserFromIntygProxyServiceImplTest {

  private static MockWebServer mockWebServer;
  private GetUserFromIntygProxyServiceImpl getUserFromIntygProxyService;

  private static final String User_ID = "191212121212";
  private static final String USER_FIRSTNAME = "Arnold";
  private static final String USER_LASTNAME = "Johansson";
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
    final var getUserEndpoint = "/api/v1/user";
    getUserFromIntygProxyService = new GetUserFromIntygProxyServiceImpl(
        WebClient.create(baseUrl), scheme, baseUrl,
        mockWebServer.getPort(), getUserEndpoint);
  }

  @Test
  void shouldReturnUserResponse() throws JsonProcessingException {
    final var personRequest = GetUserIntegrationRequest.builder().userId(User_ID).build();
    final var expectedResponse = UserResponseDTO.builder()
        .user(
            UserDTO.builder()
                .personnummer(User_ID)
                .fornamn(USER_FIRSTNAME)
                .efternamn(USER_LASTNAME)
                .build()
        )
        .status(StatusDTO.FOUND)
        .build();
    mockWebServer.enqueue(
        new MockResponse().setBody(objectMapper.writeValueAsString(expectedResponse))
            .addHeader("Content-Type", "application/json"));
    final var actualResponse = getUserFromIntygProxyService.getUserFromIntygProxy(
        personRequest);
    Assertions.assertEquals(expectedResponse, actualResponse);
  }
}
