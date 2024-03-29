package se.inera.intyg.minaintyg.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import se.inera.intyg.minaintyg.integration.common.IllegalCertificateAccessException;
import se.inera.intyg.minaintyg.integration.common.IntegrationServiceException;
import se.inera.intyg.minaintyg.logging.service.MonitoringLogService;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandlerController {

  private final MonitoringLogService monitoringLogService;

  @ExceptionHandler(IntegrationServiceException.class)
  public ResponseEntity<String> handleCommunicationError(IntegrationServiceException exception) {
    log.error("Unable to establish integration with '{}' stacktrace {}",
        exception.getApplicationName(),
        exception.getStackTrace()
    );

    return ResponseEntity
        .status(503)
        .build();
  }

  @ExceptionHandler(IllegalCertificateAccessException.class)
  public ResponseEntity<String> handleAccessException(IllegalCertificateAccessException exception) {
    monitoringLogService.logIllegalCertificateAccess(exception.getMessage());
    return ResponseEntity
        .status(403)
        .build();
  }
}
