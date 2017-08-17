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
package se.inera.intyg.minaintyg.web.security.saml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;
import se.inera.intyg.infra.integration.pu.model.Person;
import se.inera.intyg.minaintyg.web.exception.PersonNotFoundException;
import se.inera.intyg.minaintyg.web.integration.pu.MinaIntygPUService;
import se.inera.intyg.minaintyg.web.integration.pu.PersonNameUtil;
import se.inera.intyg.minaintyg.web.security.CitizenImpl;
import se.inera.intyg.schemas.contract.InvalidPersonNummerException;
import se.inera.intyg.schemas.contract.Personnummer;

import static se.inera.intyg.minaintyg.web.security.LoginMethodEnum.FK;

@Service
public class CitizenAuthenticationService implements SAMLUserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(CitizenAuthenticationService.class);

    @Autowired
    private MinaIntygPUService minaIntygPUService;

    private PersonNameUtil personNameUtil = new PersonNameUtil();

    @Override
    public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {
        String userid = credential.getNameID().getValue();
        Personnummer pnr = new Personnummer(userid);

        LOG.debug("SAML user: " + pnr.getPnrHash());

        try {
            Person person = minaIntygPUService.getPerson(pnr.getPersonnummer());
            LOG.info("Citizen authenticated and present in PU-service.");
            return new CitizenImpl(pnr.getNormalizedPnr(), FK, personNameUtil.buildFullName(person), person.isSekretessmarkering());
        } catch (PersonNotFoundException e) {
            LOG.error("Person not found in PU-service, cannot authorize access to Mina Intyg.");
            throw e;
        } catch (IllegalStateException e) {
            LOG.error("Person could not be looked up in PU-service due to technical error, "
                    + "cannot authorize access to Mina Intyg.");
            throw e;
        } catch (InvalidPersonNummerException e) {
            LOG.error("Could not transform userid from SAML credential to valid Personnummer.");
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
