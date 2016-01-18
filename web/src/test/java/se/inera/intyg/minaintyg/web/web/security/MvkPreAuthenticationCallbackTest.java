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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.callistasoftware.netcare.mvk.authentication.service.api.AuthenticationResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class MvkPreAuthenticationCallbackTest {
    @Test
    public void testLookupPrincipal() throws Exception {

        AuthenticationResult authenticationResultMock = mock(AuthenticationResult.class);
        when(authenticationResultMock.getUsername()).thenReturn("1234567890");

        MvkPreAuthenticationCallback mvkPreAuthenticationCallback = new MvkPreAuthenticationCallback();
        UserDetails userDetails = mvkPreAuthenticationCallback.lookupPrincipal(authenticationResultMock);

        assertEquals(userDetails.getClass().getCanonicalName(), CitizenImpl.class.getCanonicalName());
        assertEquals(((Citizen) userDetails).getUsername(), "1234567890");

    }
}
