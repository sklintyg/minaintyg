/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

package se.inera.intyg.minaintyg.web.web.security;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.minaintyg.web.web.service.MonitoringLogService;

@RunWith(MockitoJUnitRunner.class)
public class LoggingSessionRegistryImplTest {

    @Mock
    private MonitoringLogService monitoringService;

    @InjectMocks
    private LoggingSessionRegistryImpl loggingSessionRegistry;

    @Test
    public void testRegisterNewSession() throws Exception {
        final String sessionId = "session-id";
        final String personId = "19121212-1212";
        Citizen principal = mock(Citizen.class);
        when(principal.getUsername()).thenReturn(personId);
        when(principal.getLoginMethod()).thenReturn(LoginMethodEnum.FK);
        loggingSessionRegistry.registerNewSession(sessionId, principal);

        verify(monitoringService).logCitizenLogin(new Personnummer(personId), "FK");
    }

    @Test
    public void testRegisterNewSessionNoLoginMethod() throws Exception {
        final String sessionId = "session-id";
        final String personId = "19121212-1212";
        Citizen principal = mock(Citizen.class);
        when(principal.getUsername()).thenReturn(personId);
        when(principal.getLoginMethod()).thenReturn(null);
        loggingSessionRegistry.registerNewSession(sessionId, principal);

        verify(monitoringService).logCitizenLogin(new Personnummer(personId), null);
    }

    @Test
    public void testRegisterNewSessionNoCitizen() throws Exception {
        final String sessionId = "session-id";
        loggingSessionRegistry.registerNewSession(sessionId, "principal");

        verifyZeroInteractions(monitoringService);
    }

    @Test
    public void testRemoveSessionInformation() throws Exception {
        final String sessionId = "session-id";
        final String personId = "19121212-1212";
        Citizen principal = mock(Citizen.class);
        when(principal.getUsername()).thenReturn(personId);
        when(principal.getLoginMethod()).thenReturn(LoginMethodEnum.FK);
        loggingSessionRegistry.registerNewSession(sessionId, principal);

        loggingSessionRegistry.removeSessionInformation(sessionId);

        verify(monitoringService).logCitizenLogout(new Personnummer(personId), "FK");
    }

    @Test
    public void testRemoveSessionInformationNoLoginMethod() throws Exception {
        final String sessionId = "session-id";
        final String personId = "19121212-1212";
        Citizen principal = mock(Citizen.class);
        when(principal.getUsername()).thenReturn(personId);
        when(principal.getLoginMethod()).thenReturn(null);
        loggingSessionRegistry.registerNewSession(sessionId, principal);

        loggingSessionRegistry.removeSessionInformation(sessionId);

        verify(monitoringService).logCitizenLogout(new Personnummer(personId), null);
    }

    @Test
    public void testRemoveSessionInformationNoCitizen() throws Exception {
        final String sessionId = "session-id";
        loggingSessionRegistry.registerNewSession(sessionId, "principal");

        loggingSessionRegistry.removeSessionInformation(sessionId);

        verifyZeroInteractions(monitoringService);
    }

    @Test
    public void testRemoveSessionInformationNoSession() throws Exception {
        loggingSessionRegistry.removeSessionInformation("session-id");

        verifyZeroInteractions(monitoringService);
    }
}