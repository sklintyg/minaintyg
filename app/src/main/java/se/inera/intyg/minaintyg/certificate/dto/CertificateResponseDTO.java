package se.inera.intyg.minaintyg.certificate.dto;

import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.certificate.legacy.CertificateMetadata;

@Value
@Builder
public class CertificateResponseDTO {

  CertificateMetadata metadata;
  String content;
}
