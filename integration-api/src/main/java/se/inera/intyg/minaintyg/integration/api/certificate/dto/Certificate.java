package se.inera.intyg.minaintyg.integration.api.certificate.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Certificate {

  String id;
  CertificateType type;
  CertificateSummary summary;
  CertificateIssuer issuer;
  CertificateUnit unit;
  List<CertificateEvent> events;
  List<CertificateStatusType> statuses;
  String issued;
}
