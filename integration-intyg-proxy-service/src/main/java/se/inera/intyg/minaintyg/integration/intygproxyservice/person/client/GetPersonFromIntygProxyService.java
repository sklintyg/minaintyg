package se.inera.intyg.minaintyg.integration.intygproxyservice.person.client;

import se.inera.intyg.minaintyg.integration.api.person.GetPersonIntegrationRequest;

public interface GetPersonFromIntygProxyService {

  PersonSvarDTO getPersonFromIntygProxy(GetPersonIntegrationRequest personRequest);
}
