package se.inera.intyg.minaintyg.monitoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.logging.LogController;
import se.inera.intyg.minaintyg.logging.dto.LogErrorRequestDTO;
import se.inera.intyg.minaintyg.logging.service.LogErrorService;
import se.inera.intyg.minaintyg.logging.service.dto.LogErrorRequest;

@ExtendWith(MockitoExtension.class)
class LogControllerTest {

  private static final LogErrorRequestDTO REQUEST = LogErrorRequestDTO.builder()
      .id("id")
      .message("message")
      .code("code")
      .stackTrace("stackTrace")
      .build();

  @Mock
  private LogErrorService logErrorService;

  @InjectMocks
  private LogController logController;

  @Test
  void shouldCallServiceWithErrorId() {
    final var captor = ArgumentCaptor.forClass(LogErrorRequest.class);

    logController.logError(REQUEST);

    verify(logErrorService).log(captor.capture());
    assertEquals(REQUEST.getId(), captor.getValue().getId());
  }

  @Test
  void shouldCallServiceWithErrorCode() {
    final var captor = ArgumentCaptor.forClass(LogErrorRequest.class);

    logController.logError(REQUEST);

    verify(logErrorService).log(captor.capture());
    assertEquals(REQUEST.getCode(), captor.getValue().getCode());
  }

  @Test
  void shouldCallServiceWithErrorMessage() {
    final var captor = ArgumentCaptor.forClass(LogErrorRequest.class);

    logController.logError(REQUEST);

    verify(logErrorService).log(captor.capture());
    assertEquals(REQUEST.getMessage(), captor.getValue().getMessage());
  }

  @Test
  void shouldCallServiceWithErrorStackTrace() {
    final var captor = ArgumentCaptor.forClass(LogErrorRequest.class);

    logController.logError(REQUEST);

    verify(logErrorService).log(captor.capture());
    assertEquals(REQUEST.getStackTrace(), captor.getValue().getStackTrace());
  }
}