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
package se.inera.intyg.minaintyg.web.web.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Implementation of the @see CitizenDetails interface.
 */
@SuppressWarnings("serial")
public class CitizenImpl implements Citizen {

    private Boolean consent;
    private String username;
    private LoginMethodEnum loginMethod;

    private Set<SimpleGrantedAuthority> roles = Collections.singleton(new SimpleGrantedAuthority("ROLE_CITIZEN"));

    public CitizenImpl(String username, LoginMethodEnum loginMethod) {
        this.username = username;
        this.loginMethod = loginMethod;
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
}
