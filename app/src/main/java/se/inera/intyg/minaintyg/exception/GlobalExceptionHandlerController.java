package se.inera.intyg.minaintyg.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import se.inera.intyg.minaintyg.integration.common.IntegrationServiceException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandlerController {

  @ExceptionHandler(IntegrationServiceException.class)
  public ResponseEntity<String> handleCommunicationError(IntegrationServiceException exception) {
    log.error("Unable to establish integration with '{}' {}",
        exception.getApplicationName(),
        exception.getMessage()
    );
    return ResponseEntity
        .status(503)
        .build();
  }
}
