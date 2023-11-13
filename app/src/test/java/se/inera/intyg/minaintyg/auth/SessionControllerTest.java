/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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

package se.inera.intyg.minaintyg.auth;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

    @InjectMocks
    private SessionController sessionController;

    private static HttpServletRequest request;

    @Nested
    class WithSession {

        @BeforeEach
        void setup() {
            request = mock(HttpServletRequest.class);
            final var context = mock(SecurityContext.class);
            final var authentication = mock(Authentication.class);
            final var session = mock(HttpSession.class);

            when(request.getSession((false))).thenReturn(session);
            when(session.getAttribute(anyString())).thenReturn(context);
            when(context.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(MinaIntygUser.builder().build());
        }

        @Test
        void testGetSessionWhenSession() {
            final var sessionStatus = sessionController.getSessionStatus(request);

            assertTrue(sessionStatus.isHasSession());
        }

    }

    @Nested
    class NoSession {

        @BeforeEach
        void setup() {
            request = mock(HttpServletRequest.class);
            when(request.getSession((false))).thenReturn(null);
        }

        @Test
        void testGetSessionStatusWhenNoSession() {
            final var sessionStatus = sessionController.getSessionStatus(request);

            assertFalse(sessionStatus.isHasSession());
        }
    }

}