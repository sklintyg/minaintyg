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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.minaintyg.auth.SessionTimeoutService.LAST_ACCESS_ATTRIBUTE;
import static se.inera.intyg.minaintyg.auth.SessionTimeoutService.SECONDS_UNTIL_EXPIRE;
import static se.inera.intyg.minaintyg.auth.SessionTimeoutService.SESSION_EXPIRATION_LIMIT;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

@ExtendWith(MockitoExtension.class)
class SessionTimeoutServiceTest {

    private static final List<String> EXCLUDED_URLS = List.of("URL");
    private static final String ACTUAL_URL = "ACTUAL_URL";
    public static final long LAST_ACCESS_TIME = System.currentTimeMillis();

    HttpServletRequest request;
    MockHttpSession session = new MockHttpSession();

    @InjectMocks
    private SessionTimeoutService sessionTimeoutService;

    @BeforeEach
    void setup() {
        request = mock(HttpServletRequest.class);

        when(request.getSession(anyBoolean())).thenReturn(session);
        session.setAttribute(LAST_ACCESS_ATTRIBUTE, LAST_ACCESS_TIME);
        session.setAttribute(SECONDS_UNTIL_EXPIRE, 0L);
    }

    @Nested
    class TestInvalidatedSession {

        @BeforeEach
        void setup() {
            session.setAttribute(LAST_ACCESS_ATTRIBUTE,
                System.currentTimeMillis() - (SESSION_EXPIRATION_LIMIT + 1000)
            );
        }

        @Test
        void shouldInvalidateSessionIfExpiredIncludedURL() {
            sessionTimeoutService.checkSessionValidity(request, EXCLUDED_URLS);

            assertTrue(session.isInvalid());
        }

        @Test
        void shouldInvalidateSessionIfExpiredExcludedURL() {
            sessionTimeoutService.checkSessionValidity(request, List.of(ACTUAL_URL));

            assertTrue(session.isInvalid());
        }
    }

    @Nested
    class TestValidSession {

        @BeforeEach
        void setup() {
            when(request.getRequestURI()).thenReturn(ACTUAL_URL);
        }

        @Test
        void shouldNotCreateNewSessionWhenCallingGetSession() {
            final var captor = ArgumentCaptor.forClass(Boolean.class);
            sessionTimeoutService.checkSessionValidity(request, EXCLUDED_URLS);

            verify(request).getSession(captor.capture());
            assertFalse(captor.getValue());

        }

        @Test
        void shouldResetLastAccessedTimeIfIncludedUrl() {
            sessionTimeoutService.checkSessionValidity(request, EXCLUDED_URLS);

            assertEquals(System.currentTimeMillis(), (Long) session.getAttribute(LAST_ACCESS_ATTRIBUTE));
            assertNotEquals(LAST_ACCESS_TIME, (Long) session.getAttribute(LAST_ACCESS_ATTRIBUTE));

        }

        @Test
        void shouldNotResetLastAccessedTimeIfExcludedUrl() {
            sessionTimeoutService.checkSessionValidity(request, List.of(ACTUAL_URL));

            assertEquals(LAST_ACCESS_TIME, session.getAttribute(LAST_ACCESS_ATTRIBUTE));
        }

        @Test
        void shouldNotInvalidateSessionIfNotExpiredIncludedURL() {
            sessionTimeoutService.checkSessionValidity(request, EXCLUDED_URLS);

            assertFalse(session.isInvalid());
        }

        @Test
        void shouldNotInvalidateSessionIfNotExpiredExcludedURL() {
            sessionTimeoutService.checkSessionValidity(request, List.of(ACTUAL_URL));

            assertFalse(session.isInvalid());
        }

        @Test
        void shouldSetMsUntilExpireForExcludedUrl() {
            sessionTimeoutService.checkSessionValidity(request, List.of(ACTUAL_URL));

            assertTrue((Long) session.getAttribute(SECONDS_UNTIL_EXPIRE) < SESSION_EXPIRATION_LIMIT);
            assertTrue((Long) session.getAttribute(SECONDS_UNTIL_EXPIRE) > 0);
        }

        @Test
        void shouldSetMsUntilExpireForIncludedUrl() {
            sessionTimeoutService.checkSessionValidity(request, EXCLUDED_URLS);

            assertEquals(SESSION_EXPIRATION_LIMIT, session.getAttribute(SECONDS_UNTIL_EXPIRE));
        }
    }
}