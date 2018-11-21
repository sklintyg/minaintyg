/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.minaintyg.web.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;
import se.inera.intyg.schemas.contract.Personnummer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MonitoringLogServiceImplTest {

    private static final Personnummer USER_ID = Personnummer.createPersonnummer("20121212-1212").get();

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
        verifyLog(Level.INFO, "CITIZEN_LOGIN Citizen 'aa9f5f25483e54eedd25f7a2a225b7834e54db9ee39695cf1c49881cf2bca381' logged in using login method 'LOGIN_METHOD'");
    }

    private void verifyLog(Level logLevel, String logMessage) {
        // Verify and capture logging interaction
        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();

        // Verify log
        assertThat(loggingEvent.getLevel(), equalTo(logLevel));
        assertThat(loggingEvent.getFormattedMessage(),
                equalTo(logMessage));
    }

    @Test
    public void shouldCitizenLogout() {
        logService.logCitizenLogout(USER_ID, LOGIN_METHOD);
        verifyLog(Level.INFO, "CITIZEN_LOGOUT Citizen 'aa9f5f25483e54eedd25f7a2a225b7834e54db9ee39695cf1c49881cf2bca381' logged out using login method 'LOGIN_METHOD'");
    }

    @Test
    public void shouldLogCitizenConsentGiven() {
        logService.logCitizenConsentGiven(USER_ID);
        verifyLog(
                Level.INFO,
                "CONSENT_GIVEN Consent given by citizen 'aa9f5f25483e54eedd25f7a2a225b7834e54db9ee39695cf1c49881cf2bca381'");
    }

    @Test
    public void shouldLogCitizenConsentRevoked() {
        logService.logCitizenConsentRevoked(USER_ID);
        verifyLog(Level.INFO, "CONSENT_REVOKED Consent revoked by citizen 'aa9f5f25483e54eedd25f7a2a225b7834e54db9ee39695cf1c49881cf2bca381'");
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
