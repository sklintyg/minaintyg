package se.inera.intyg.minaintyg.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.logging.dto.LogErrorRequestDTO;
import se.inera.intyg.minaintyg.logging.service.LogErrorService;
import se.inera.intyg.minaintyg.logging.service.dto.LogErrorRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/log")
public class LogController {

  private final LogErrorService logErrorService;

  @PostMapping("/error")
  public void logError(@RequestBody LogErrorRequestDTO request) {
    log.debug("Logging error with id: '{}'", request.getId());
    logErrorService.log(
        LogErrorRequest.builder()
            .id(request.getId())
            .code(request.getCode())
            .message(request.getMessage())
            .stackTrace(request.getStackTrace())
            .build()
    );
  }
}
