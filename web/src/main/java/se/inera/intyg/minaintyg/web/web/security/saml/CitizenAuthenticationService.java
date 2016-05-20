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

package se.inera.intyg.minaintyg.web.web.security.saml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;

import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.minaintyg.web.web.security.CitizenImpl;
import se.inera.intyg.minaintyg.web.web.security.LoginMethodEnum;

@Service
public class CitizenAuthenticationService implements SAMLUserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(CitizenAuthenticationService.class);

    @Override
    public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {
        String userid = credential.getNameID().getValue();
        LOG.debug("SAML user: " + new Personnummer(userid).getPnrHash());
        // CHECKSTYLE:OFF MagicNumber
        return new CitizenImpl(userid.substring(0, 8) + "-" + userid.substring(8), LoginMethodEnum.FK);
        // CHECKSTYLE:ON MagicNumber
    }

}
