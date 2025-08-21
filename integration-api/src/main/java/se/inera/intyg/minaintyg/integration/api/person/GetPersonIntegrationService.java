package se.inera.intyg.minaintyg.integration.api.person;

import se.inera.intyg.minaintyg.integration.api.PersonIntegrationResponse;

public interface GetPersonIntegrationService {

  PersonIntegrationResponse getPerson(GetPersonIntegrationRequest personRequest);
}