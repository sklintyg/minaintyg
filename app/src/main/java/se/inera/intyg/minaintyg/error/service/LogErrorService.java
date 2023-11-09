package se.inera.intyg.minaintyg.error.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.error.service.dto.LogErrorRequest;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;
import se.inera.intyg.minaintyg.user.UserService;

@RequiredArgsConstructor
@Service
public class LogErrorService {

  private final MonitoringLogService monitoringLogService;
  private final UserService userService;

  public void log(LogErrorRequest request) {
    if (userService.getLoggedInUser().isEmpty()) {
      throw new IllegalStateException("Error cannot be logged if user is not defined");
    }

    final var error = request.getError();
    monitoringLogService.logClientError(
        error.getId(),
        error.getCode(),
        error.getMessage(),
        error.getStackTrace()
    );
  }
}
