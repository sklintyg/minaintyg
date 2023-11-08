package se.inera.intyg.minaintyg.integration.webcert.client.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.AvailableFunction;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateResponseDTO {

  private CertificateDTO certificate;
  private List<AvailableFunction> availableFunctions;
}
