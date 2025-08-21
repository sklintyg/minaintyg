package se.inera.intyg.minaintyg.integration.api.person;

import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.PersonIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.person.model.Person;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;

@Value
@Builder
public class GetPersonIntegrationResponse implements PersonIntegrationResponse {

  Person person;
  Status status;
}