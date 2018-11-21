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
package se.inera.intyg.minaintyg.web.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import se.inera.intyg.minaintyg.web.security.Citizen;
import se.inera.intyg.minaintyg.web.security.CitizenImpl;
import se.inera.intyg.minaintyg.web.security.LoginMethodEnum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CitizenServiceTest {

    private static final String PERSON_FULL_NAME = "Tolvan Tolvansson";
    private CitizenService service = new CitizenService();

    @Test
    public void testGetCitizen() {
        final String username = "1234567890";
        Citizen user = new CitizenImpl(username, LoginMethodEnum.FK, PERSON_FULL_NAME, false);
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Citizen res = service.getCitizen();
        assertNotNull(res);
        assertEquals(username, res.getUsername());
        assertEquals(LoginMethodEnum.FK, res.getLoginMethod());
    }

    @Test
    public void testGetCitizenNoCitizen() {
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn("1234567890");
        SecurityContextHolder.getContext().setAuthentication(auth);

        Citizen res = service.getCitizen();
        assertNull(res);
    }

}
