package se.inera.intyg.minaintyg.integration.api.certificate.model;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificatesRequest {

  String patientId;
  List<String> years;
  List<String> units;
  List<String> certificateTypes;
  List<CertificateStatusType> statuses;
}
