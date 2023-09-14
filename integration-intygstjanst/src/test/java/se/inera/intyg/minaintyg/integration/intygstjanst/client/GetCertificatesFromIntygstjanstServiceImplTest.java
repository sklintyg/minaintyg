package se.inera.intyg.minaintyg.integration.intygstjanst.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificatesRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificatesResponse;

import java.io.IOException;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class GetCertificatesFromIntygstjanstServiceImplTest {
    private static MockWebServer mockWebServer;
    private GetCertificatesFromIntygstjanstServiceImpl getCertificatesFromIntygstjanstService;

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
        final var endpoint = "/api/certificate";
        getCertificatesFromIntygstjanstService = new GetCertificatesFromIntygstjanstServiceImpl(
                WebClient.create(baseUrl), scheme, baseUrl,
                mockWebServer.getPort(), endpoint);
    }

    @Test
    void shouldReturnCertificatesResponse() throws JsonProcessingException {
        final var request = CertificatesRequest.builder().build();
        final var expectedResponse = CertificatesResponse
                .builder()
                .content(List.of(Certificate
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