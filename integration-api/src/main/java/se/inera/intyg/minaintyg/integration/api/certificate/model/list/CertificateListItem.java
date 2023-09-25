package se.inera.intyg.minaintyg.integration.api.certificate.model.list;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificateListItem {

  String id;
  CertificateType type;
  CertificateSummary summary;
  CertificateIssuer issuer;
  CertificateUnit unit;
  List<CertificateEvent> events;
  List<CertificateStatusType> statuses;
  LocalDateTime issued;
}
