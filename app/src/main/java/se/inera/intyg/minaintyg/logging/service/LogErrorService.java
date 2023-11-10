package se.inera.intyg.minaintyg.logging.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.logging.service.dto.LogErrorRequest;
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

    monitoringLogService.logClientError(
        request.getId(),
        request.getCode(),
        request.getMessage(),
        request.getStackTrace()
    );
  }
}
