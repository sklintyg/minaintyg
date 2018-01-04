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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.ws.WebServiceException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.session.SessionRegistry;

import se.inera.intyg.minaintyg.web.service.dto.HealthStatus;
import se.riv.itintegration.monitoring.rivtabp21.v1.PingForConfigurationResponderInterface;
import se.riv.itintegration.monitoring.v1.PingForConfigurationResponseType;
import se.riv.itintegration.monitoring.v1.PingForConfigurationType;

@RunWith(MockitoJUnitRunner.class)
public class MonitoringServiceImplTest {

    @Mock
    private PingForConfigurationResponderInterface intygstjanstPingForConfiguration;

    @Mock
    private SessionRegistry sessionRegistry;

    @InjectMocks
    private MonitoringServiceImpl service;

    @Test
    public void testCheckIntygstjanst() {
        when(intygstjanstPingForConfiguration.pingForConfiguration(anyString(), any(PingForConfigurationType.class))).thenReturn(new PingForConfigurationResponseType());

        HealthStatus res = service.checkIntygstjanst();

        assertNotNull(res);
        assertTrue(res.isOk());
        assertNotNull(res.getMeasurement());
    }

    @Test
    public void testCheckIntygstjanstNoResponse() {
        when(intygstjanstPingForConfiguration.pingForConfiguration(anyString(), any(PingForConfigurationType.class))).thenReturn(null);

        HealthStatus res = service.checkIntygstjanst();

        assertNotNull(res);
        assertFalse(res.isOk());
        assertEquals(-1, res.getMeasurement());
    }

    @Test
    public void testCheckIntygstjanstException() {
        when(intygstjanstPingForConfiguration.pingForConfiguration(anyString(), any(PingForConfigurationType.class))).thenThrow(new WebServiceException());

        HealthStatus res = service.checkIntygstjanst();

        assertNotNull(res);
        assertFalse(res.isOk());
        assertEquals(-1, res.getMeasurement());
    }

    @Test
    public void testGetNbrOfLoggedInUsers() {
        when(sessionRegistry.getAllPrincipals()).thenReturn(Arrays.asList("user"));
        HealthStatus res = service.getNbrOfLoggedInUsers();

        assertNotNull(res);
        assertTrue(res.isOk());
        assertEquals(1, res.getMeasurement());
    }

    @Test
    public void testGetNbrOfLoggedInUsersNoUsers() {
        when(sessionRegistry.getAllPrincipals()).thenReturn(new ArrayList<>());
        HealthStatus res = service.getNbrOfLoggedInUsers();

        assertNotNull(res);
        assertTrue(res.isOk());
        assertEquals(0, res.getMeasurement());
    }
}
