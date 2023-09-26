package se.inera.intyg.minaintyg.certificate.service.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListCertificatesRequest {

  private List<String> years;
  private List<String> units;
  private List<String> certificateTypes;
  private List<CertificateStatusType> statuses;
}
