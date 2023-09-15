package se.inera.intyg.minaintyg.certificate.service.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateStatusType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListCertificatesRequest {

  List<String> years;
  List<String> units;
  List<String> certificateTypes;
  List<CertificateStatusType> statuses;
}
