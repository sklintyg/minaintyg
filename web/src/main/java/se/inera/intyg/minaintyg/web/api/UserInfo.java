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
package se.inera.intyg.minaintyg.web.api;

import java.io.Serializable;

/**
 * Created by marced on 2017-05-11.
 */
@SuppressWarnings("serial")
public class UserInfo implements Serializable {
    private String personId;
    private String fullName;
    private String loginMethod;
    private boolean sekretessmarkering;
    private boolean consentGiven;

    public UserInfo(String personId, String fullName, String loginMethod, boolean sekretessmarkering, boolean consentGiven) {
        this.personId = personId;
        this.fullName = fullName;
        this.loginMethod = loginMethod;
        this.sekretessmarkering = sekretessmarkering;
        this.consentGiven = consentGiven;
    }

    public String getPersonId() {
        return personId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLoginMethod() {
        return loginMethod;
    }

    public boolean isSekretessmarkering() {
        return sekretessmarkering;
    }

    public boolean isConsentGiven() {
        return consentGiven;
    }
}