package se.inera.intyg.minaintyg.integration.intygproxyservice.citizen.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CitizenResponseDTO {

  private CitizenDTO citizen;
  private StatusDTO status;
}
