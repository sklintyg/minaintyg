/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.controller.monitoring;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import javax.ws.rs.core.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.infra.monitoring.logging.UserAgentInfo;
import se.inera.intyg.infra.monitoring.logging.UserAgentParser;
import se.inera.intyg.minaintyg.web.controller.monitoring.MonitoringRequest.MonitoringRequestEvent;
import se.inera.intyg.minaintyg.web.service.MonitoringLogService;


@RunWith(MockitoJUnitRunner.class)
public class MonitoringApiControllerTest {

    protected static final String OS_FAMILY = "OS_FAMILY";
    protected static final String OS_VERSION = "OS_VERSION";
    private static final String BROWSERNAME = "lynx";
    private static final String BROWSER_VERSION = "1.0";
    private static final String WIDTH = "100";
    private static final String HEIGHT = "200";

    @Mock
    private MonitoringLogService monitoringService;

    @Mock
    private UserAgentParser userAgentParser;

    @InjectMocks
    private MonitoringApiController controller;

    @Test
    public void testMonitoring() {
        when(userAgentParser.parse(anyString())).thenReturn(new UserAgentInfo(BROWSERNAME, BROWSER_VERSION, OS_FAMILY, OS_VERSION));
        MonitoringRequest request = new MonitoringRequest();
        request.setEvent(MonitoringRequestEvent.SCREEN_RESOLUTION);
        request.setInfo(ImmutableMap.of(MonitoringRequest.WIDTH, WIDTH, MonitoringRequest.HEIGHT, HEIGHT));

        Response response = controller.monitoring(request, "some userAgent header");

        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        verify(monitoringService).logBrowserInfo(BROWSERNAME, BROWSER_VERSION, OS_FAMILY, OS_VERSION, WIDTH, HEIGHT);
    }

}