package se.inera.intyg.minaintyg.integration.api.citizen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCitizenIntegrationRequest {

  private String citizenId;
}
