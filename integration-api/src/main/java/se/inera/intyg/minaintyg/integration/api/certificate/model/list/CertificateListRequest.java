package se.inera.intyg.minaintyg.integration.api.certificate.model.list;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificateListRequest {

  String patientId;
  List<String> years;
  List<String> units;
  List<String> certificateTypes;
  List<CertificateStatusType> statuses;
}
