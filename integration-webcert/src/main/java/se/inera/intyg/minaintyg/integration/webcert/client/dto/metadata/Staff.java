package se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Staff {

  private String personId;
  private String fullName;
  private String prescriptionCode;

}
