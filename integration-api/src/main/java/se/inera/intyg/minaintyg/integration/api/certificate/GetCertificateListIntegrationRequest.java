package se.inera.intyg.minaintyg.integration.api.certificate;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;

@Value
@Builder
public class GetCertificateListIntegrationRequest {

  String patientId;
  List<String> years;
  List<String> units;
  List<String> certificateTypes;
  List<CertificateStatusType> statuses;
}
