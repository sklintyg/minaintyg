package se.inera.intyg.minaintyg.integration.api.citizen;

import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.citizen.model.Citizen;
import se.inera.intyg.minaintyg.integration.api.citizen.model.Status;

@Value
@Builder
public class GetCitizenIntegrationResponse {

  Citizen citizen;
  Status status;
}
