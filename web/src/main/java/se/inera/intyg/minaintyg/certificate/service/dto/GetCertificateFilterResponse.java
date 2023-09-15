package se.inera.intyg.minaintyg.certificate.service.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateUnit;

@Data
@Builder
public class GetCertificateFilterResponse {

  List<String> years;
  List<CertificateUnit> units;
  List<String> certificateTypes;
  List<CertificateStatusType> statuses;
}
