package se.inera.intyg.minaintyg.certificate.service.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateUnit;

@Data
@Builder
public class GetCertificateFilterResponse {

  List<String> years;
  List<CertificateUnit> units;
  List<CertificateType> certificateTypes;
  List<CertificateStatusType> statuses;
}
