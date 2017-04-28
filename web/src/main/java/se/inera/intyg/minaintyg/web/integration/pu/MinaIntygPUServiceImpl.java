package se.inera.intyg.minaintyg.web.integration.pu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.inera.intyg.infra.integration.pu.model.Person;
import se.inera.intyg.infra.integration.pu.model.PersonSvar;
import se.inera.intyg.infra.integration.pu.services.PUService;
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
            LOG.error("Person identified by '{}' not found i PU-service", pnr.getPnrHash());
            throw new PersonNotFoundException("Person identified by '" + pnr.getPnrHash() + "' not found i PU-service");
        } else {
            LOG.error("Error communicating with PU service, cannot query person '{}'", pnr.getPnrHash());
            throw new IllegalStateException("Error communicating with PU service, cannot query person '" + pnr.getPnrHash() + "'");
        }
    }

}
