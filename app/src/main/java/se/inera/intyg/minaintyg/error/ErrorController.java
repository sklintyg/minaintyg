package se.inera.intyg.minaintyg.error;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.minaintyg.error.dto.LogErrorRequestDTO;
import se.inera.intyg.minaintyg.error.service.LogErrorService;
import se.inera.intyg.minaintyg.error.service.dto.LogErrorRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/error")
public class ErrorController {

  private final LogErrorService logErrorService;

  @PostMapping
  public void logError(LogErrorRequestDTO request) {
    log.debug("Logging error with id: '{}'", request.getError().getId());

    logErrorService.log(
        LogErrorRequest.builder()
            .error(request.getError())
            .build()
    );
  }
}
