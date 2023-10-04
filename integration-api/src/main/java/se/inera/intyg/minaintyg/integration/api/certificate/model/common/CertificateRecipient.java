package se.inera.intyg.minaintyg.integration.api.certificate.model.common;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificateRecipient {

  String id;
  String name;
  LocalDateTime sent;
}
