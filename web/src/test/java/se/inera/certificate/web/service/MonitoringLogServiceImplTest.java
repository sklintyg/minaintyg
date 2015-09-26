package se.inera.certificate.web.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

@RunWith(MockitoJUnitRunner.class)
public class MonitoringLogServiceImplTest {

    private static final String USER_ID = "USER_ID";
    private static final String LOGIN_METHOD = "LOGIN_METHOD";
    private static final String CERTIFICATE_ID = "CERTIFICATE_ID";
    private static final String CERTIFICATE_TYPE = "CERTIFICATE_TYPE";
    private static final String RECIPIENT_ID = "RECIPIENT_ID";

    @Mock
    private Appender<ILoggingEvent> mockAppender;

    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;
    
    MonitoringLogService logService = new MonitoringLogServiceImpl();

    @Before
    public void setup() {
        
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);
    }
    
    @After
    public void teardown() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(mockAppender);
    }
    
    @Test
    public void shouldLogCitizenLogin() {
        logService.logCitizenLogin(USER_ID, LOGIN_METHOD);
        verifyLog(Level.INFO, "CITIZEN_LOGIN Citizen 'e5bb97d1792ff76e360cd8e928b6b9b53bda3e4fe88b026e961c2facf963a361' logged in using login method 'LOGIN_METHOD'");
    }

    private void verifyLog(Level logLevel, String logMessage) {
        // Verify and capture logging interaction
        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

        // Verify log
        assertThat(loggingEvent.getLevel(), is(logLevel));
        assertThat(loggingEvent.getFormattedMessage(), 
                is(logMessage));
    }

    @Test
    public void shouldCitizenLogout() {
        logService.logCitizenLogout(USER_ID, LOGIN_METHOD);
        verifyLog(Level.INFO, "CITIZEN_LOGOUT Citizen 'e5bb97d1792ff76e360cd8e928b6b9b53bda3e4fe88b026e961c2facf963a361' logged out using login method 'LOGIN_METHOD'");
    }

    @Test
    public void shouldLogCitizenConsentGiven() {
        logService.logCitizenConsentGiven(USER_ID);
        verifyLog(
                Level.INFO,
                "CONSENT_GIVEN Consent given by citizen 'e5bb97d1792ff76e360cd8e928b6b9b53bda3e4fe88b026e961c2facf963a361'");
    }

    @Test
    public void shouldLogCitizenConsentRevoked() {
        logService.logCitizenConsentRevoked(USER_ID);
        verifyLog(Level.INFO, "CONSENT_REVOKED Consent revoked by citizen 'e5bb97d1792ff76e360cd8e928b6b9b53bda3e4fe88b026e961c2facf963a361'");
    }

    @Test
    public void shouldLogCertificateRead() {
        logService.logCertificateRead(CERTIFICATE_ID, CERTIFICATE_TYPE);
        verifyLog(Level.INFO, "CERTIFICATE_READ Certificate 'CERTIFICATE_ID' of type 'CERTIFICATE_TYPE' was read");
    }

    @Test
    public void shouldLogCertificateSend() {
        logService.logCertificateSend(CERTIFICATE_ID, RECIPIENT_ID);
        verifyLog(Level.INFO, "CERTIFICATE_SEND Certificate 'CERTIFICATE_ID' sent to 'RECIPIENT_ID'");
    }

    @Test
    public void shouldLogCertificateArchived() {
        logService.logCertificateArchived(CERTIFICATE_ID);
        verifyLog(Level.INFO, "CERTIFICATE_ARCHIVED Certificate 'CERTIFICATE_ID' archived");
    }

    @Test
    public void shouldLogCertificateRestored() {
        logService.logCertificateRestored(CERTIFICATE_ID);
        verifyLog(Level.INFO, "CERTIFICATE_RESTORED Certificate 'CERTIFICATE_ID' restored");
    }
}
