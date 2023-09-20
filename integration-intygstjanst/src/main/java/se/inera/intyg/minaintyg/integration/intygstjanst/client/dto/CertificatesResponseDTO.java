package se.inera.intyg.minaintyg.integration.intygstjanst.client.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificatesResponseDTO {

  private List<CertificateDTO> content;
}