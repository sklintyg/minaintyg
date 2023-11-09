package se.inera.intyg.minaintyg.monitoring;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
import se.inera.intyg.minaintyg.auth.LoginMethod;
import se.inera.intyg.minaintyg.logging.MonitoringLogServiceImpl;
import se.inera.intyg.minaintyg.util.HashUtility;

@ExtendWith(MockitoExtension.class)
class MonitoringLogServiceImplTest {

  private static final String PERSON_ID = "personId";
  @InjectMocks
  private MonitoringLogServiceImpl monitoringLogService;
  @Captor
  private ArgumentCaptor<LoggingEvent> captorLoggingEvent;
  @Mock
  private Appender<ILoggingEvent> mockAppender;

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

  private void verifyLog(Level logLevel, String logMessage) {
    verify(mockAppender).doAppend(captorLoggingEvent.capture());
    final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
    assertThat(loggingEvent.getLevel(), equalTo(logLevel));
    assertThat(loggingEvent.getFormattedMessage(),
        equalTo(logMessage));
  }

  @Nested
  class LogUserLogin {

    @Test
    void shouldLogWhenUserLogin() {
      monitoringLogService.logUserLogin(PERSON_ID, LoginMethod.ELVA77.name());
      final var hashedPersonId = HashUtility.hash(PERSON_ID);
      verifyLog(Level.INFO,
          "CITIZEN_LOGIN Citizen '" + hashedPersonId + "' logged in using login method 'ELVA77'");
    }
  }

  @Nested
  class LogUserLogout {

    @Test
    void shouldLogWhenUserLogout() {
      monitoringLogService.logUserLogout(PERSON_ID, LoginMethod.ELVA77.name());
      final var hashedPersonId = HashUtility.hash(PERSON_ID);
      verifyLog(Level.INFO,
          "CITIZEN_LOGOUT Citizen '" + hashedPersonId + "' logged out using login method 'ELVA77'");
    }
  }

  @Nested
  class LogCertificate {

    @Test
    void shouldLogWhenUserListsCertificate() {
      monitoringLogService.logListCertificates(PERSON_ID, 10);
      final var hashedPersonId = HashUtility.hash(PERSON_ID);
      verifyLog(Level.INFO,
          "LIST_CERTIFICATES Citizen '" + hashedPersonId + "' listed '10' certificates");
    }

    @Test
    void shouldLogWhenCertificateIsRead() {
      monitoringLogService.logCertificateRead("id", "lisjp");

      verifyLog(Level.INFO, "CERTIFICATE_READ Certificate 'id' of type 'lisjp' was read");
    }

    @Test
    void shouldLogWhenCertificateIsSent() {
      monitoringLogService.logCertificateSent("id", "lisjp", "recipient");

      verifyLog(Level.INFO,
          "CERTIFICATE_SEND Certificate 'id' of type 'lisjp' sent to 'recipient'");
    }
  }

  @Nested
  class LogClientError {

    @Test
    void shouldLogClientErrorWithStackTrace() {
      monitoringLogService.logClientError("id", "code", "message", "stack trace");

      verifyLog(Level.ERROR,
          "CLIENT_ERROR Received error from client with errorId 'id' with error code 'code', message 'message' and stacktrace 'stack trace'");
    }

    @Test
    void shouldLogClientErrorWithNoStackTrace() {
      monitoringLogService.logClientError("id", "code", "message", null);

      verifyLog(Level.ERROR,
          "CLIENT_ERROR Received error from client with errorId 'id' with error code 'code', message 'message' and stacktrace 'NO_STACK_TRACE'");
    }
    
    @Test
    void shouldLogClientErrorWithEmptyStackTrace() {
      monitoringLogService.logClientError("id", "code", "message", "");

      verifyLog(Level.ERROR,
          "CLIENT_ERROR Received error from client with errorId 'id' with error code 'code', message 'message' and stacktrace 'NO_STACK_TRACE'");
    }
  }
}
