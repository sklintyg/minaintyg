package se.inera.intyg.minaintyg.integration.pu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import se.inera.intyg.minaintyg.integration.dto.Person;
import se.inera.intyg.minaintyg.integration.dto.PersonRequest;
import se.inera.intyg.minaintyg.integration.dto.PersonResponse;
import se.inera.intyg.minaintyg.integration.dto.Status;

@ExtendWith(MockitoExtension.class)
class PUIntegrationServiceImplTest {

    private static MockWebServer mockWebServer;
    private PUIntegrationServiceImpl puIntegrationService;

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
        puIntegrationService = new PUIntegrationServiceImpl(WebClient.create(baseUrl), scheme, baseUrl,
            mockWebServer.getPort(), getPersonEndpoint);
    }
    @Test
    void shouldReturnPersonResponse() throws JsonProcessingException {
        final var personRequest = PersonRequest.builder().personId(PERSON_ID).build();
        final var expectedResponse = PersonResponse.builder()
            .person(
                Person.builder()
                    .personnummer(PERSON_ID)
                    .fornamn(PERSON_FIRSTNAME)
                    .efternamn(PERSON_LASTNAME)
                    .build()
            )
            .status(Status.FOUND)
            .build();
        mockWebServer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(expectedResponse))
            .addHeader("Content-Type", "application/json"));
        final var actualResponse = puIntegrationService.getPersonResponse(personRequest);
        assertEquals(expectedResponse, actualResponse);
    }
}