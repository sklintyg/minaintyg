package se.inera.intyg.minaintyg.certificate.service.dto;

import lombok.Builder;
import lombok.Data;
import se.inera.intyg.minaintyg.certificate.legacy.CertificateMetadata;

@Data
@Builder
public class FormattedCertificate {

  private CertificateMetadata metadata;
  private String formattedContent;
}
