package se.inera.intyg.minaintyg.integration.api.person;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonResponse {

  Person person;
  Status status;
}
