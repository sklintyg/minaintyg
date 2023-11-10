package se.inera.intyg.minaintyg.integration.common;

import java.util.function.Function;
import org.springframework.web.reactive.function.client.WebClientRequestException;

public class ExceptionThrowableFunction {

  private ExceptionThrowableFunction() {
    throw new IllegalStateException("Utility class");
  }

  public static Function<WebClientRequestException, Throwable> get(
      String applicationName) {
    return throwable -> IntegrationServiceException.builder()
        .applicationName(applicationName)
        .message(throwable.getMessage())
        .build();
  }
}
