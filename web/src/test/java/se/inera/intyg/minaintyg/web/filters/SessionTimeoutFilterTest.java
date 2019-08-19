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
package se.inera.intyg.minaintyg.web.filters;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SessionTimeoutFilterTest {

    private static final String TEST_URI = "/test";
    private static final int FIVE_SECONDS_AGO = 5000;
    private static final int ONE_SECOND = 1;
    private static final int HALF_AN_HOUR = 1800;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpSession session;

    @Test
    public void testDoFilterInternalWillInvalidateAnExpiredSession() throws Exception {
        // Arrange
        setupMocks(ONE_SECOND, TEST_URI);
        SessionTimeoutFilter filter = new SessionTimeoutFilter();
        filter.setGetSessionStatusUri(TEST_URI);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(session).invalidate();
        verify(session, never()).setAttribute(any(), any());

    }

    @Test
    public void testDoFilterInternalWillNotInvalidateValidSession() throws Exception {
        // Arrange
        SessionTimeoutFilter filter = new SessionTimeoutFilter();
        filter.setGetSessionStatusUri(TEST_URI);
        setupMocks(HALF_AN_HOUR, "anotherurl");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(session, never()).invalidate();
        verify(session).setAttribute(eq(SessionTimeoutFilter.LAST_ACCESS_TIME_ATTRIBUTE_NAME), any());

    }

    private void setupMocks(int sessionLengthInSeconds, String reportedRequestURI) {

        when(request.getSession(false)).thenReturn(session);
        when(request.getRequestURI()).thenReturn(reportedRequestURI);
        when(session.getAttribute(eq(SessionTimeoutFilter.LAST_ACCESS_TIME_ATTRIBUTE_NAME)))
            .thenReturn(new Long(System.currentTimeMillis() - FIVE_SECONDS_AGO));
        when(session.getMaxInactiveInterval()).thenReturn(sessionLengthInSeconds);

    }
}
