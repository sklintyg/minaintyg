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
package se.inera.intyg.minaintyg.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Implementation of the @see CitizenDetails interface.
 */
@SuppressWarnings("serial")
public class CitizenImpl implements Citizen {

    private Boolean consent;
    private String username;
    private LoginMethodEnum loginMethod;
    private String fullName;
    private boolean sekretessmarkering = false;

    private Set<SimpleGrantedAuthority> roles = Collections.singleton(new SimpleGrantedAuthority("ROLE_CITIZEN"));

    public CitizenImpl(String username, LoginMethodEnum loginMethod, String fullName, boolean sekretessmarkering) {
        this.username = username;
        this.loginMethod = loginMethod;
        this.fullName = fullName;
        this.sekretessmarkering = sekretessmarkering;
    }

    @Override
    public Boolean hasConsent() {
        if (consentIsKnown()) {
            return consent;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean consentIsKnown() {
        return consent != null;
    }

    @Override
    public void setConsent(boolean consent) {
        this.consent = consent;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public LoginMethodEnum getLoginMethod() {
        return loginMethod;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean isSekretessmarkering() {
        return sekretessmarkering;
    }
}
