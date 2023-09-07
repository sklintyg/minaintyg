package se.inera.intyg.minaintyg.integration.intygproxyservice.person.client;

import se.inera.intyg.minaintyg.integration.api.person.PersonRequest;
import se.inera.intyg.minaintyg.integration.api.person.PersonResponse;

public interface GetPersonFromIntygProxyService {

    PersonResponse getPerson(PersonRequest personRequest);
}
