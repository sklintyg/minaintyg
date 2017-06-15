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
package se.inera.intyg.minaintyg.web.web.security;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 */
public class CitizenImplTest {

    private static final String PERSON_FULL_NAME = "Tolvan Tolvansson";
    private static final String USERNAME = "username";

    @Test
    public void testHasNoConsent() throws Exception {
        Citizen citizen = new CitizenImpl(USERNAME, LoginMethodEnum.ELVA77, PERSON_FULL_NAME, false);
        Assert.assertFalse(citizen.hasConsent());
        Assert.assertFalse(citizen.consentIsKnown());
    }

    @Test
    public void testHasConsentSet() throws Exception {
        Citizen citizen = new CitizenImpl("username", LoginMethodEnum.ELVA77, PERSON_FULL_NAME, false);
        citizen.setConsent(false);
        Assert.assertTrue(citizen.consentIsKnown());
        Assert.assertFalse(citizen.hasConsent());
        citizen.setConsent(true);
        Assert.assertTrue(citizen.hasConsent());
    }

    @Test
    public void testReturnsDefaultRole() {
        Citizen citizen = new CitizenImpl("username", LoginMethodEnum.ELVA77, PERSON_FULL_NAME, false);
        Assert.assertTrue(citizen.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CITIZEN")));
    }
   
    @Test
    public void testGetUsername() {
        Citizen citizen = new CitizenImpl(USERNAME, LoginMethodEnum.ELVA77, PERSON_FULL_NAME, false);
        Assert.assertEquals(USERNAME, citizen.getUsername());
    }
    
    @Test
    public void testLoginMethod() {
        Citizen citizen = new CitizenImpl(USERNAME, LoginMethodEnum.ELVA77, PERSON_FULL_NAME, false);
        Assert.assertEquals(LoginMethodEnum.ELVA77, citizen.getLoginMethod());
    }

}
