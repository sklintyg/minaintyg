package se.inera.intyg.minaintyg.integration.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClientRequestException;

class ExceptionThrowableFunctionTest {

  private static final String EXPECTED_APPLICATION_NAME = "applicationName";
  private WebClientRequestException webClientRequestException;

  @BeforeEach
  void setUp() {
    webClientRequestException = new WebClientRequestException(
        new RuntimeException(),
        HttpMethod.GET,
        URI.create("test"),
        HttpHeaders.EMPTY
    );
  }

  @Test
  void shouldReturnExceptionIncludingApplicationName() {
    final var function = ExceptionThrowableFunction.get(EXPECTED_APPLICATION_NAME);
    final var throwable = function.apply(webClientRequestException);
    final var result = (IntegrationServiceException) throwable;
    assertEquals(EXPECTED_APPLICATION_NAME, result.getApplicationName());
  }

  @Test
  void shouldReturnExceptionIncludingMessage() {
    final var function = ExceptionThrowableFunction.get(EXPECTED_APPLICATION_NAME);
    final var throwable = function.apply(webClientRequestException);
    final var result = (IntegrationServiceException) throwable;
    assertEquals(webClientRequestException.getMessage(), result.getMessage());
  }
}