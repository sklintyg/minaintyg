package se.inera.intyg.minaintyg.integration.webcert.client.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.AvailableFunctionType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.availablefunction.InformationDTO;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableFunctionDTO {

  private AvailableFunctionType type;
  private String name;
  private String description;
  private String title;
  private String body;
  private List<InformationDTO> information;
}
