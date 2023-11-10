package se.inera.intyg.minaintyg.logging.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.logging.service.dto.LogErrorRequest;

@RequiredArgsConstructor
@Service
public class LogErrorService {

  private final MonitoringLogService monitoringLogService;

  public void log(LogErrorRequest request) {
    monitoringLogService.logClientError(
        request.getId(),
        request.getCode(),
        request.getMessage(),
        request.getStackTrace()
    );
  }
}
