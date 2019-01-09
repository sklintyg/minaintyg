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

import java.io.Serializable;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * The details of a citizen.
 */
public interface Citizen extends UserDetails, Serializable {

    /**
     * Should return the login methos used to access the application.
     * @return {@link LoginMethodEnum} Login method used
     */
    LoginMethodEnum getLoginMethod();

    /**
     * Returns the full name of the person.
     */
    String getFullName();

    /**
     * Returns true if person has sekretessmarkering according to the PU service.
     */
    boolean isSekretessmarkering();

}
