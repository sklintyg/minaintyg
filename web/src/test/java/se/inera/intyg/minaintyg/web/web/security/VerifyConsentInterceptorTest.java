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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.intyg.minaintyg.web.web.service.CitizenService;

@RunWith(MockitoJUnitRunner.class)
public class VerifyConsentInterceptorTest {

    @Mock
    private CitizenService service = mock(CitizenService.class);

    @InjectMocks
    private VerifyConsentInterceptor interceptor = new VerifyConsentInterceptor();

    @Test
    public void testPrehandleNoConsentNoJson() throws Exception {
        Citizen citizen = new CitizenImpl("123456789", LoginMethodEnum.MVK);
        citizen.setConsent(false);
        when(service.getCitizen()).thenReturn(citizen);

        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletOutputStream servletOutputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        boolean preHandle = interceptor.preHandle(null, response, null);
        assertFalse(preHandle);
        verify(response).sendRedirect(Mockito.anyString());

    }

    @Test
    public void testPrehandleDoesNothingWhenConsentGiven() throws Exception {
        Citizen citizen = new CitizenImpl("123456789", LoginMethodEnum.MVK);
        citizen.setConsent(true);
        when(service.getCitizen()).thenReturn(citizen);

        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletOutputStream servletOutputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        boolean preHandle = interceptor.preHandle(null, response, null);
        assertTrue(preHandle);
        Mockito.verifyZeroInteractions(response);

    }

}
