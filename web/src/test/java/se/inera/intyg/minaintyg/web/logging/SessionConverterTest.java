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
package se.inera.intyg.minaintyg.web.logging;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import ch.qos.logback.classic.spi.LoggingEvent;

public class SessionConverterTest {

    SessionConverter sessionConverter = new SessionConverter();

    @Before
    public void setup() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void testConvertWithoutSession() {
        String res = sessionConverter.convert(new LoggingEvent());

        assertEquals("NO SESSION", res);
    }

    @Test
    public void testConvert() {
        final String sessionId = "SESSION ID";
        RequestAttributes attributes = mock(RequestAttributes.class);
        when(attributes.getSessionId()).thenReturn(sessionId);
        RequestContextHolder.setRequestAttributes(attributes);
        String res = sessionConverter.convert(new LoggingEvent());

        assertEquals(sessionId, res);
    }
}
