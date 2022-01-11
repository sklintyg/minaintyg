/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.controller.session;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import se.inera.intyg.minaintyg.web.security.CitizenImpl;
import se.inera.intyg.minaintyg.web.security.LoginMethodEnum;

@RunWith(MockitoJUnitRunner.class)
public class SessionStatusControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private SecurityContext context;

    @Mock
    private Authentication authentication;

    @Mock
    private HttpSession session;

    @InjectMocks
    private SessionStatusController controller = new SessionStatusController();

    @Test
    public void testGetSessionStatusOk() {
        // Arrange
        when(request.getSession((false))).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(context);
        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new CitizenImpl("test", LoginMethodEnum.FK, "test", false));

        // Act
        final GetSessionStatusResponse sessionStatus = controller.getSessionStatus(request);

        // Assert
        assertTrue(sessionStatus.isHasSession());
        assertTrue(sessionStatus.isAuthenticated());
    }

    @Test
    public void testGetSessionStatusNoSession() {
        // Arrange
        when(request.getSession((false))).thenReturn(null);

        // Act
        final GetSessionStatusResponse sessionStatus = controller.getSessionStatus(request);

        // Assert
        assertFalse(sessionStatus.isHasSession());
        assertFalse(sessionStatus.isAuthenticated());
    }
}
