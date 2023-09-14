package se.inera.intyg.minaintyg.integration.intygproxyservice.person.client;

import se.inera.intyg.minaintyg.integration.api.person.PersonRequest;

public interface GetPersonFromIntygProxyService {

  PersonSvarDTO getPersonFromIntygProxy(PersonRequest personRequest);
}
