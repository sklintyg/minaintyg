package se.inera.intyg.minaintyg.integration.api.certificate.model.list;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificateEvent {

  LocalDateTime timestamp;
  String certificateId;
  String description;
}
