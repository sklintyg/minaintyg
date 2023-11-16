package se.inera.intyg.minaintyg.integration.webcert.client.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateResponseDTO {

  private CertificateDTO certificate;
  private List<AvailableFunctionDTO> availableFunctions;
  private List<CertificateTextDTO> texts;
}
