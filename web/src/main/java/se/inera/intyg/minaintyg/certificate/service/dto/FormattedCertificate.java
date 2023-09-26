package se.inera.intyg.minaintyg.certificate.service.dto;

import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;

@Value
@Builder
public class FormattedCertificate {

  CertificateMetadata metadata;
  String formattedContent;
}
