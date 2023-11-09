package se.inera.intyg.minaintyg.integration.webcert.client.dto.availablefunction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.InformationType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InformationDTO {

  private String id;
  private String text;
  private InformationType type;
}
