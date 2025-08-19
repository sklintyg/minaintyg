package se.inera.intyg.minaintyg.integration.intygproxyservice.person.client;

import se.inera.intyg.minaintyg.integration.api.person.GetUserIntegrationRequest;

public interface GetUserFromIntygProxyService {

  UserResponseDTO getUserFromIntygProxy(GetUserIntegrationRequest userRequest);
}
