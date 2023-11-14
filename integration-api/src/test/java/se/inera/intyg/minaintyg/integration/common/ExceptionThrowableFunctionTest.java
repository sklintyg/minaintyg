package se.inera.intyg.minaintyg.integration.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

class ExceptionThrowableFunctionTest {

  private static final String EXPECTED_APPLICATION_NAME = "applicationName";
  private static final String TEST = "test";
  private WebClientRequestException webClientRequestException;
  private WebClientResponseException webClientResponseException;

  @BeforeEach
  void setUp() {
    webClientRequestException = new WebClientRequestException(
        new RuntimeException(),
        HttpMethod.GET,
        URI.create(TEST),
        HttpHeaders.EMPTY
    );
    webClientResponseException = new WebClientResponseException(
        504,
        "",
        null,
        null,
        null
    );
  }

  @Nested
  class WebClientRequestExceptions {

    @Test
    void shouldReturnExceptionIncludingApplicationName() {
      final var function = ExceptionThrowableFunction.webClientRequest(EXPECTED_APPLICATION_NAME);
      final var throwable = function.apply(webClientRequestException);
      final var result = (IntegrationServiceException) throwable;
      assertEquals(EXPECTED_APPLICATION_NAME, result.getApplicationName());
    }

    @Test
    void shouldReturnExceptionIncludingMessage() {
      final var function = ExceptionThrowableFunction.webClientRequest(EXPECTED_APPLICATION_NAME);
      final var throwable = function.apply(webClientRequestException);
      final var result = (IntegrationServiceException) throwable;
      assertEquals(webClientRequestException.getMessage(), result.getMessage());
    }
  }

  @Nested
  class GatewayTimeout {

    @Test
    void shouldReturnExceptionIncludingApplicationName() {
      final var function = ExceptionThrowableFunction.gatewayTimeout(EXPECTED_APPLICATION_NAME);
      final var throwable = function.apply(webClientResponseException);
      final var result = (IntegrationServiceException) throwable;
      assertEquals(EXPECTED_APPLICATION_NAME, result.getApplicationName());
    }

    @Test
    void shouldReturnExceptionIncludingMessage() {
      final var function = ExceptionThrowableFunction.gatewayTimeout(EXPECTED_APPLICATION_NAME);
      final var throwable = function.apply(webClientResponseException);
      final var result = (IntegrationServiceException) throwable;
      assertEquals(webClientResponseException.getMessage(), result.getMessage());
    }
  }
}
