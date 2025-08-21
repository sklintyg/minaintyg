package se.inera.intyg.minaintyg.integration.api;

import se.inera.intyg.minaintyg.integration.api.person.model.Person;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;

public interface PersonIntegrationResponse {

  Status getStatus();

  Person getPerson();

}