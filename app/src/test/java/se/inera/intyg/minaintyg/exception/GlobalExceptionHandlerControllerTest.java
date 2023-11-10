package se.inera.intyg.minaintyg.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import se.inera.intyg.minaintyg.integration.common.IntegrationServiceException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerControllerTest {

  private static final String APPLICATION_NAME = "appname";
  private static final String MESSAGE = "message";
  private static final String EXPECTED_MESSAGE = "Unable to establish integration with 'appname' message";
  @Captor
  private ArgumentCaptor<LoggingEvent> captorLoggingEvent;
  @Mock
  private Appender<ILoggingEvent> mockAppender;
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
        IntegrationServiceException.builder().build());
    assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
  }

  @Test
  void shouldLogWithErrorLevel() {
    globalExceptionHandlerController.handleCommunicationError(
        IntegrationServiceException.builder()
            .build()
    );
    verify(mockAppender).doAppend(captorLoggingEvent.capture());
    assertEquals(Level.ERROR, captorLoggingEvent.getValue().getLevel());
  }

  @Test
  void shouldLogUsingProvidedIntegrationServiceException() {
    globalExceptionHandlerController.handleCommunicationError(
        IntegrationServiceException.builder()
            .applicationName(APPLICATION_NAME)
            .message(MESSAGE)
            .build()
    );
    verify(mockAppender).doAppend(captorLoggingEvent.capture());
    assertThat(captorLoggingEvent.getValue().getFormattedMessage(),
        equalTo(EXPECTED_MESSAGE));
  }
}