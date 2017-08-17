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
import org.callistasoftware.netcare.mvk.authentication.service.api.PreAuthenticationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import se.inera.intyg.infra.integration.pu.model.Person;
import se.inera.intyg.minaintyg.web.exception.PersonNotFoundException;
import se.inera.intyg.minaintyg.web.integration.pu.MinaIntygPUService;
import se.inera.intyg.minaintyg.web.integration.pu.PersonNameUtil;

import static se.inera.intyg.minaintyg.web.security.LoginMethodEnum.MVK;

@Service
public class MvkPreAuthenticationCallback implements PreAuthenticationCallback {

    private static final Logger LOG = LoggerFactory.getLogger(MvkPreAuthenticationCallback.class);

    @Autowired
    private MinaIntygPUService minaIntygPUService;

    private PersonNameUtil personNameUtil = new PersonNameUtil();

    @Override
    public UserDetails createMissingUser(AuthenticationResult preAuthenticated) {
        return null;
    }

    @Override
    public UserDetails verifyPrincipal(Object principal) {
        return (UserDetails) principal;
    }

    @Override
    public UserDetails lookupPrincipal(AuthenticationResult auth) {
        LOG.info("Citizen authenticated.");
        try {
            Person person = minaIntygPUService.getPerson(auth.getUsername());
            return new CitizenImpl(auth.getUsername(), MVK, personNameUtil.buildFullName(person), person.isSekretessmarkering());
        } catch (PersonNotFoundException e) {
            LOG.error("Person not found in PU-service, cannot authorize access to Mina Intyg.");
            throw e;
        } catch (IllegalStateException e) {
            LOG.error("Person could not be looked up in PU-service due to technical error, "
                    + "cannot authorize access to Mina Intyg.");
            throw e;
        }
    }
}
