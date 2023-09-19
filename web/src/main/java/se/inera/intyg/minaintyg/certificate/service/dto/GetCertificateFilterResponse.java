package se.inera.intyg.minaintyg.certificate.service.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateUnit;

@Value
@Builder
public class GetCertificateFilterResponse {

  List<String> years;
  List<CertificateUnit> units;
  List<CertificateType> certificateTypes;
  List<CertificateStatusType> statuses;
}
