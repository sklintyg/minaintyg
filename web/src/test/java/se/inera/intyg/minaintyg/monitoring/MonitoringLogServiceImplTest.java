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

  @InjectMocks
  private MonitoringLogServiceImpl monitoringLogService;
  @Captor
  private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

  @Mock
  private Appender<ILoggingEvent> mockAppender;

  private static final String PERSON_ID = "personId";

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


  private void verifyLog(Level logLevel, String logMessage) {
    verify(mockAppender).doAppend(captorLoggingEvent.capture());
    final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
    assertThat(loggingEvent.getLevel(), equalTo(logLevel));
    assertThat(loggingEvent.getFormattedMessage(),
        equalTo(logMessage));
  }
}