/**
 * Copyright (C) 2012 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Web (http://code.google.com/p/inera-certificate-web).
 *
 * Inera Certificate Web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.web.security;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import se.inera.certificate.web.security.Citizen;
import se.inera.certificate.web.security.CitizenImpl;

/**
 */
public class CitizenImplTest {

    @Test
    public void testHasNoConsent() throws Exception {
        Citizen citizen = new CitizenImpl("username");
        Assert.assertFalse(citizen.hasConsent());
        Assert.assertFalse(citizen.consentIsKnown());
    }

    @Test
    public void testHasConsentSet() throws Exception {
        Citizen citizen = new CitizenImpl("username");
        citizen.setConsent(false);
        Assert.assertTrue(citizen.consentIsKnown());
        Assert.assertFalse(citizen.hasConsent());
        citizen.setConsent(true);
        Assert.assertTrue(citizen.hasConsent());
    }

    @Test
    public void testReturnsDefaultRole() {
        Citizen citizen = new CitizenImpl("username");
        Assert.assertTrue(citizen.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CITIZEN")));
    }
    @Test
    public void testGetUsername() {
        String user = "username";
        Citizen citizen = new CitizenImpl(user);
        Assert.assertEquals(user, citizen.getUsername());
    }
}
