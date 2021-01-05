/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@RunWith(MockitoJUnitRunner.class)
public class BrowserClosedInterceptorTest {

    private static final String REDIRECT_LOCATION = "/home/login";
    private static final Clock FIXED_TIME_CLOCK = Clock.fixed(Instant.ofEpochMilli(0), ZoneId.systemDefault());

    @Mock
    private LogoutHandler logoutHandler = mock(LogoutHandler.class);

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @InjectMocks
    private BrowserClosedInterceptor interceptor = new BrowserClosedInterceptor();

    @Before
    public void init() {
        interceptor.setMockSystemClock(FIXED_TIME_CLOCK);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);

        interceptor.setRedirectLocation(REDIRECT_LOCATION);
        interceptor.setTimeoutSeconds(5);
    }

    @Test
    public void testValidRefresh() throws Exception {

        when(session.getAttribute(BrowserClosedInterceptor.BROWSER_CLOSED_TIMESTAMP)).thenReturn(getOffsetTime(1));

        boolean preHandle = interceptor.preHandle(request, response, null);

        Mockito.verifyNoInteractions(logoutHandler);
        assertTrue(preHandle);

    }

    @Test
    public void testNormalRequest() throws Exception {

        boolean preHandle = interceptor.preHandle(request, response, null);

        Mockito.verifyNoInteractions(logoutHandler);
        assertTrue(preHandle);

    }

    @Test
    public void testOldInvalidRequest() throws Exception {

        when(session.getAttribute(BrowserClosedInterceptor.BROWSER_CLOSED_TIMESTAMP)).thenReturn(getOffsetTime(10));

        boolean preHandle = interceptor.preHandle(request, response, null);

        Mockito.verify(logoutHandler).logout(request, response, null);
        Mockito.verify(response).sendRedirect(REDIRECT_LOCATION);
        assertFalse(preHandle);

    }

    public LocalDateTime getOffsetTime(int secondOffset) {
        return LocalDateTime.now(FIXED_TIME_CLOCK).minusSeconds(secondOffset);
    }

}
