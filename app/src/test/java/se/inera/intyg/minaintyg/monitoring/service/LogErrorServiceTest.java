package se.inera.intyg.minaintyg.monitoring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.logging.service.LogErrorService;
import se.inera.intyg.minaintyg.logging.service.MonitoringLogService;
import se.inera.intyg.minaintyg.logging.service.dto.LogErrorRequest;

@ExtendWith(MockitoExtension.class)
class LogErrorServiceTest {

  private static final LogErrorRequest REQUEST = LogErrorRequest.builder()
      .id("ID")
      .code("CODE")
      .message("MESSAGE")
      .stackTrace("STACK TRACE")
      .build();

  @Mock
  MonitoringLogService monitoringLogService;

  @InjectMocks
  private LogErrorService logErrorService;

  @Test
  void shouldLogUsingId() {
    final var captor = ArgumentCaptor.forClass(String.class);

    logErrorService.log(REQUEST);

    verify(monitoringLogService).logClientError(
        captor.capture(),
        anyString(),
        anyString(),
        anyString()
    );
    assertEquals(REQUEST.getId(), captor.getValue());
  }

  @Test
  void shouldLogUsingCode() {
    final var captor = ArgumentCaptor.forClass(String.class);

    logErrorService.log(REQUEST);

    verify(monitoringLogService).logClientError(
        anyString(),
        captor.capture(),
        anyString(),
        anyString()
    );
    assertEquals(REQUEST.getCode(), captor.getValue());
  }

  @Test
  void shouldLogUsingMessage() {
    final var captor = ArgumentCaptor.forClass(String.class);

    logErrorService.log(REQUEST);

    verify(monitoringLogService).logClientError(
        anyString(),
        anyString(),
        captor.capture(),
        anyString()
    );
    assertEquals(REQUEST.getMessage(), captor.getValue());
  }

  @Test
  void shouldLogUsingStackTrace() {
    final var captor = ArgumentCaptor.forClass(String.class);

    logErrorService.log(REQUEST);

    verify(monitoringLogService).logClientError(
        anyString(),
        anyString(),
        anyString(),
        captor.capture()
    );
    assertEquals(REQUEST.getStackTrace(), captor.getValue());
  }
}