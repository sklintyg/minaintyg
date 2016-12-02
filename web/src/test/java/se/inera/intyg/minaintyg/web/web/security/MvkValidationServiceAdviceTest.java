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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.aspectj.lang.ProceedingJoinPoint;
import org.callistasoftware.netcare.mvk.authentication.service.api.AuthenticationRequest;
import org.callistasoftware.netcare.mvk.authentication.service.api.AuthenticationResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MvkValidationServiceAdviceTest {

    MvkValidationServiceAdvice mvkValidationServiceAdvice = new MvkValidationServiceAdvice();

    @Before
    public void setup() {
        mvkValidationServiceAdvice.setFakeMatcherRegExp("[12]{1}[90]{1}[0-9]{6}-?[0-9]{4}");
    }

    @Test
    public void testOverrideMVKAuthFakeLogin() throws Throwable {
        final String personId = "19121212-1212";
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        AuthenticationRequest req = mock(AuthenticationRequest.class);
        when(req.getAuthenticationToken()).thenReturn(personId);
        when(joinPoint.getArgs()).thenReturn(new Object[] {req});
        AuthenticationResult res = (AuthenticationResult) mvkValidationServiceAdvice.overrideMVKAuth(joinPoint);
        assertNotNull(res);
        assertEquals(personId, res.getUsername());
        verify(joinPoint, never()).proceed();
    }

    @Test
    public void testOverrideMVKAuthFormatsUsernameForFakeLogin() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        AuthenticationRequest req = mock(AuthenticationRequest.class);
        when(req.getAuthenticationToken()).thenReturn("191212121212");
        when(joinPoint.getArgs()).thenReturn(new Object[] {req});
        AuthenticationResult res = (AuthenticationResult) mvkValidationServiceAdvice.overrideMVKAuth(joinPoint);
        assertNotNull(res);
        assertEquals("19121212-1212", res.getUsername());
        verify(joinPoint, never()).proceed();
    }

    @Test
    public void testOverrideMVKAuthNullToken() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        AuthenticationRequest req = mock(AuthenticationRequest.class);
        when(req.getAuthenticationToken()).thenReturn(null);
        when(joinPoint.getArgs()).thenReturn(new Object[] {req});
        mvkValidationServiceAdvice.overrideMVKAuth(joinPoint);

        verify(joinPoint).proceed();
    }

    @Test
    public void testOverrideMVKAuthNonMatchingToken() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        AuthenticationRequest req = mock(AuthenticationRequest.class);
        when(req.getAuthenticationToken()).thenReturn("sdf43rj34tfjworel43k4jtl4kj2lk4jtl42jtl42f2");
        when(joinPoint.getArgs()).thenReturn(new Object[] {req});
        mvkValidationServiceAdvice.overrideMVKAuth(joinPoint);

        verify(joinPoint).proceed();
    }
}
