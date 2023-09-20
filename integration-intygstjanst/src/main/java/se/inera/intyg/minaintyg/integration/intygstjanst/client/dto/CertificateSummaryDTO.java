package se.inera.intyg.minaintyg.integration.intygstjanst.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateSummaryDTO {

  private String label;
  private String value;
}
