package se.inera.intyg.minaintyg.integration.common;

import java.util.function.Function;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class ExceptionThrowableFunction {

  private ExceptionThrowableFunction() {
    throw new IllegalStateException("Utility class");
  }

  public static Function<WebClientRequestException, Throwable> webClientRequest(
      String applicationName) {
    return throwable -> new IntegrationServiceException(
        throwable.getMessage(),
        throwable,
        applicationName
    );
  }

  public static Function<WebClientResponseException, Throwable> gatewayTimeout(
      String applicationName) {
    return throwable -> new IntegrationServiceException(
        throwable.getMessage(),
        throwable,
        applicationName
    );
  }
}
