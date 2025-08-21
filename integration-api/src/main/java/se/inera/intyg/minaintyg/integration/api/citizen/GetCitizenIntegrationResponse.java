package se.inera.intyg.minaintyg.integration.api.citizen;

import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.PersonIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.person.model.Person;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;

@Value
@Builder
public class GetCitizenIntegrationResponse implements PersonIntegrationResponse {

  Person citizen;
  Status status;

  @Override
  public Person getPerson() {
    return citizen;
  }
}