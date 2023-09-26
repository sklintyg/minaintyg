package se.inera.intyg.minaintyg.integration.api.certificate.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateEvent;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateIssuer;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateSummary;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateUnit;

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
