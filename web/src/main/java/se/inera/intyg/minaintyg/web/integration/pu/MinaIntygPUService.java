package se.inera.intyg.minaintyg.web.integration.pu;

import se.inera.intyg.infra.integration.pu.model.Person;

/**
 * Created by eriklupander on 2017-04-25.
 */
public interface MinaIntygPUService {
    Person getPerson(String personId);
}
