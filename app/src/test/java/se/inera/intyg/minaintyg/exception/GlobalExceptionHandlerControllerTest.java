package se.inera.intyg.minaintyg.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import se.inera.intyg.minaintyg.integration.common.IllegalCertificateAccessException;
import se.inera.intyg.minaintyg.integration.common.IntegrationServiceException;
import se.inera.intyg.minaintyg.logging.service.MonitoringLogService;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerControllerTest {

  private static final String APPLICATION_NAME = "appname";
  private static final String MESSAGE = "message";
  @Captor
  private ArgumentCaptor<LoggingEvent> captorLoggingEvent;
  @Mock
  private Appender<ILoggingEvent> mockAppender;
  @Mock
  private MonitoringLogService monitoringLogService;
  @InjectMocks
  private GlobalExceptionHandlerController globalExceptionHandlerController;

  @BeforeEach
  void setUp() {
    final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    logger.addAppender(mockAppender);
  }

  @AfterEach
  void tearDown() {
    final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    logger.detachAppender(mockAppender);
  }

  @Test
  void shouldReturnStatusServiceUnavailable() {
    final var response = globalExceptionHandlerController.handleCommunicationError(
        new IntegrationServiceException(MESSAGE, new IllegalArgumentException(), APPLICATION_NAME));
    assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
  }

  @Test
  void shouldLogWithErrorLevel() {
    globalExceptionHandlerController.handleCommunicationError(
        new IntegrationServiceException(MESSAGE, new IllegalArgumentException(), APPLICATION_NAME)
    );
    verify(mockAppender).doAppend(captorLoggingEvent.capture());
    assertEquals(Level.ERROR, captorLoggingEvent.getValue().getLevel());
  }

  @Nested
  class IllegalAccess {

    @Test
    void shouldReturnStatusForbidden() {
      final var response = globalExceptionHandlerController.handleAccessException(
          new IllegalCertificateAccessException(MESSAGE)
      );

      assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void shouldLogUsingMonitorLog() {
      final var captor = ArgumentCaptor.forClass(String.class);
      globalExceptionHandlerController.handleAccessException(
          new IllegalCertificateAccessException(MESSAGE)
      );

      verify(monitoringLogService).logIllegalCertificateAccess(captor.capture());
      assertEquals(MESSAGE, captor.getValue());
    }
  }
}
