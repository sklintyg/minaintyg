package se.inera.intyg.minaintyg.monitoring.service;

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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.minaintyg.auth.LoginMethod;
import se.inera.intyg.minaintyg.logging.HashUtility;
import se.inera.intyg.minaintyg.logging.service.MonitoringLogService;

@ExtendWith(MockitoExtension.class)
class MonitoringLogServiceTest {

  private static final String PERSON_ID = "personId";
  private static final String SOMETHING_WENT_WRONG = "something went wrong";
  @InjectMocks
  private MonitoringLogService monitoringLogService;
  @Captor
  private ArgumentCaptor<LoggingEvent> captorLoggingEvent;
  @Mock
  private Appender<ILoggingEvent> mockAppender;
  @Spy
  private HashUtility hashUtility;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(hashUtility, "salt", "salt");
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
      final var hashedPersonId = hashUtility.hash(PERSON_ID);
      verifyLog(Level.INFO,
          "CITIZEN_LOGIN Citizen '" + hashedPersonId + "' logged in using login method 'ELVA77'");
    }
  }

  @Nested
  class LogUserLoginFailed {

    @Test
    void shouldLogWhenUserLoginFailed() {
      monitoringLogService.logUserLoginFailed(SOMETHING_WENT_WRONG, LoginMethod.ELVA77.name());
      verifyLog(Level.INFO,
          "CITIZEN_LOGIN_FAILURE Citizen failed to login, exception message '"
              + SOMETHING_WENT_WRONG + "'");
    }
  }

  @Nested
  class LogUserLogout {

    @Test
    void shouldLogWhenUserLogout() {
      monitoringLogService.logUserLogout(PERSON_ID, LoginMethod.ELVA77.name());
      final var hashedPersonId = hashUtility.hash(PERSON_ID);
      verifyLog(Level.INFO,
          "CITIZEN_LOGOUT Citizen '" + hashedPersonId + "' logged out using login method 'ELVA77'");
    }
  }

  @Nested
  class LogCertificate {

    @Test
    void shouldLogWhenUserListsCertificate() {
      monitoringLogService.logListCertificates(PERSON_ID, 10);
      final var hashedPersonId = hashUtility.hash(PERSON_ID);
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

    @Test
    void shouldLogWhenCertificateIsPrintedMinimal() {
      monitoringLogService.logCertificatePrinted("ID", "TYPE", false);

      verifyLog(Level.INFO,
          "CERTIFICATE_PRINTED_EMPLOYER_COPY Certificate 'ID' of type 'TYPE' was printed as employer copy");
    }

    @Test
    void shouldLogWhenCertificateIsPrintedFull() {
      monitoringLogService.logCertificatePrinted("ID", "TYPE", true);

      verifyLog(Level.INFO,
          "CERTIFICATE_PRINTED_FULLY Certificate 'ID' of type 'TYPE' was printed including all information");
    }
  }

  @Nested
  class LogClientError {

    @Test
    void shouldLogClientErrorWithStackTrace() {
      monitoringLogService.logClientError("id", "code", "message", "stack trace");

      verifyLog(Level.INFO,
          "CLIENT_ERROR Received error from client with errorId 'id' with error code 'code', message 'message' and stacktrace 'stack trace'");
    }

    @Test
    void shouldLogClientErrorWithNoStackTrace() {
      monitoringLogService.logClientError("id", "code", "message", null);

      verifyLog(Level.INFO,
          "CLIENT_ERROR Received error from client with errorId 'id' with error code 'code', message 'message' and stacktrace 'NO_STACK_TRACE'");
    }

    @Test
    void shouldLogClientErrorWithEmptyStackTrace() {
      monitoringLogService.logClientError("id", "code", "message", "");

      verifyLog(Level.INFO,
          "CLIENT_ERROR Received error from client with errorId 'id' with error code 'code', message 'message' and stacktrace 'NO_STACK_TRACE'");
    }
  }
}
