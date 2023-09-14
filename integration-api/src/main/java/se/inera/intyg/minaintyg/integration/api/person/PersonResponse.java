package se.inera.intyg.minaintyg.integration.api.person;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PersonResponse {

  Person person;
  Status status;
}
