package se.inera.intyg.minaintyg.error;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.error.dto.ErrorDataDTO;
import se.inera.intyg.minaintyg.error.dto.LogErrorRequestDTO;
import se.inera.intyg.minaintyg.error.service.LogErrorService;
import se.inera.intyg.minaintyg.error.service.dto.LogErrorRequest;

@ExtendWith(MockitoExtension.class)
class ErrorControllerTest {

  private static final LogErrorRequestDTO REQUEST = LogErrorRequestDTO.builder()
      .error(ErrorDataDTO.builder()
          .id("id")
          .message("message")
          .code("code")
          .stackTrace("stackTrace")
          .build())
      .build();

  @Mock
  private LogErrorService logErrorService;

  @InjectMocks
  private ErrorController errorController;

  @Test
  void shouldCallServiceWithErrorId() {
    final var captor = ArgumentCaptor.forClass(LogErrorRequest.class);

    errorController.logError(REQUEST);

    verify(logErrorService).log(captor.capture());
    assertEquals(REQUEST.getError().getId(), captor.getValue().getError().getId());
  }

  @Test
  void shouldCallServiceWithErrorCode() {
    final var captor = ArgumentCaptor.forClass(LogErrorRequest.class);

    errorController.logError(REQUEST);

    verify(logErrorService).log(captor.capture());
    assertEquals(REQUEST.getError().getCode(), captor.getValue().getError().getCode());
  }

  @Test
  void shouldCallServiceWithErrorMessage() {
    final var captor = ArgumentCaptor.forClass(LogErrorRequest.class);

    errorController.logError(REQUEST);

    verify(logErrorService).log(captor.capture());
    assertEquals(REQUEST.getError().getMessage(), captor.getValue().getError().getMessage());
  }

  @Test
  void shouldCallServiceWithErrorStackTrace() {
    final var captor = ArgumentCaptor.forClass(LogErrorRequest.class);

    errorController.logError(REQUEST);

    verify(logErrorService).log(captor.capture());
    assertEquals(REQUEST.getError().getStackTrace(), captor.getValue().getError().getStackTrace());
  }
}