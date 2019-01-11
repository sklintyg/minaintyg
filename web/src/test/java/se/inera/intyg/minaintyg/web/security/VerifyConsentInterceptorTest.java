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
package se.inera.intyg.minaintyg.web.security;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import se.inera.intyg.minaintyg.web.service.CitizenService;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VerifyConsentInterceptorTest {

    private static final String PERSON_FULL_NAME = "Tolvan Tolvansson";
    private static final String PNR_TOLVAN = "19121212-1212";

    @Mock
    private CitizenService service = mock(CitizenService.class);

    @InjectMocks
    private VerifyConsentInterceptor interceptor = new VerifyConsentInterceptor();

    @Test
    public void testPrehandleNoConsentNoJson() {
        Citizen citizen = new CitizenImpl(PNR_TOLVAN, LoginMethodEnum.ELVA77, PERSON_FULL_NAME, false);
        citizen.setConsent(false);
        when(service.getCitizen()).thenReturn(citizen);

        HttpServletResponse response = mock(HttpServletResponse.class);

        boolean preHandle = interceptor.preHandle(null, response, null);
        assertTrue(preHandle);
        Mockito.verifyZeroInteractions(response);
    }

    @Test
    public void testPrehandleDoesNothingWhenConsentGiven() {
        Citizen citizen = new CitizenImpl(PNR_TOLVAN, LoginMethodEnum.ELVA77, PERSON_FULL_NAME, false);
        citizen.setConsent(true);
        when(service.getCitizen()).thenReturn(citizen);

        HttpServletResponse response = mock(HttpServletResponse.class);

        boolean preHandle = interceptor.preHandle(null, response, null);
        assertTrue(preHandle);
        Mockito.verifyZeroInteractions(response);

    }

}
