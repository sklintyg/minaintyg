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

import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;

/**
 * The details of a citizen.
 */
public interface Citizen extends UserDetails, Serializable {

    /**
     * Does the citizen have consent?
     *
     * @return a boolean
     */
    Boolean hasConsent();

    /**
     * @return
     */
    boolean consentIsKnown();

    /**
     * Set consent.
     *
     * @param consent
     */
    void setConsent(boolean consent);

}