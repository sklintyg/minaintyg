package se.inera.intyg.minaintyg.integration.api.certificate.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificateUnit {

  String id;
  String name;
}
