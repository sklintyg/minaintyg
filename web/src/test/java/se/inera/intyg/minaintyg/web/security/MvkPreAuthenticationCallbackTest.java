/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

import org.callistasoftware.netcare.mvk.authentication.service.api.AuthenticationResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import se.inera.intyg.infra.integration.pu.model.Person;
import se.inera.intyg.minaintyg.web.integration.pu.MinaIntygPUService;
import se.inera.intyg.schemas.contract.Personnummer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class MvkPreAuthenticationCallbackTest {

    private static final String PERSON_ID = "19121212-1212";

    @Mock
    private MinaIntygPUService minaIntygPUService;

    @InjectMocks
    private MvkPreAuthenticationCallback testee;

    @Test
    public void testLookupPrincipal() throws Exception {

        when(minaIntygPUService.getPerson(PERSON_ID))
                .thenReturn(new Person(new Personnummer(PERSON_ID), false, false, "", "", "", "", "", ""));

        AuthenticationResult authenticationResultMock = mock(AuthenticationResult.class);
        when(authenticationResultMock.getUsername()).thenReturn(PERSON_ID);

        new MvkPreAuthenticationCallback();
        UserDetails userDetails = testee.lookupPrincipal(authenticationResultMock);

        assertEquals(userDetails.getClass().getCanonicalName(), CitizenImpl.class.getCanonicalName());
        assertEquals(((Citizen) userDetails).getUsername(), PERSON_ID);

    }
}