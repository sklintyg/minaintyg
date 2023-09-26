package se.inera.intyg.minaintyg.certificate.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateTypeFilter;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateUnit;

@Value
@Builder
public class CertificateListFilterResponseDTO {

  List<String> years;
  List<CertificateUnit> units;
  List<CertificateTypeFilter> certificateTypes;
  List<CertificateStatusType> statuses;
}
