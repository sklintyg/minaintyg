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
package se.inera.intyg.minaintyg.web.integration.pu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.inera.intyg.infra.integration.pu.model.Person;
import se.inera.intyg.infra.integration.pu.model.PersonSvar;
import se.inera.intyg.infra.integration.pu.services.PUService;
import se.inera.intyg.minaintyg.web.exception.PUServiceErrorException;
import se.inera.intyg.minaintyg.web.exception.PersonNotFoundException;
import se.inera.intyg.schemas.contract.Personnummer;

/**
 * Created by eriklupander on 2017-04-25.
 */
@Service
public class MinaIntygPUServiceImpl implements MinaIntygPUService {

    private static final Logger LOG = LoggerFactory.getLogger(MinaIntygPUServiceImpl.class);

    @Autowired
    private PUService puService;

    @Override
    public Person getPerson(String personId) {
        Personnummer pnr = new Personnummer(personId);
        PersonSvar personSvar = puService.getPerson(pnr);
        if (personSvar.getStatus() == PersonSvar.Status.FOUND) {
            return personSvar.getPerson();
        } else if (personSvar.getStatus() == PersonSvar.Status.NOT_FOUND) {
            LOG.error("Person identified by '{}' not found i PU-service, cannot login.", pnr.getPnrHash());
            throw new PersonNotFoundException("Person identified by '" + pnr.getPnrHash() + "' not found i PU-service");
        } else {
            LOG.warn("Error communicating with PU service, cannot query person '{}'", pnr.getPnrHash());
            throw new PUServiceErrorException("PU-service returns ");
        }
    }

    public void setPuService(PUService puService) {
        this.puService = puService;
    }
}
