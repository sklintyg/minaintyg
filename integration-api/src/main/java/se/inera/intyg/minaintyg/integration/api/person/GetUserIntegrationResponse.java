package se.inera.intyg.minaintyg.integration.api.person;

import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.person.model.User;
import se.inera.intyg.minaintyg.integration.api.person.model.Status;

@Value
@Builder
public class GetUserIntegrationResponse {

  User user;
  Status status;
}
