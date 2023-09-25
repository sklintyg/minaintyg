package se.inera.intyg.minaintyg.certificate.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.list.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.list.CertificateTypeFilter;
import se.inera.intyg.minaintyg.integration.api.certificate.model.list.CertificateUnit;

@Value
@Builder
public class CertificateListFilterResponseDTO {

  List<String> years;
  List<CertificateUnit> units;
  List<CertificateTypeFilter> certificateTypes;
  List<CertificateStatusType> statuses;
}
