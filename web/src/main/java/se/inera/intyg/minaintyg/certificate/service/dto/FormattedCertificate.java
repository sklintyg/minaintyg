package se.inera.intyg.minaintyg.certificate.service.dto;

import lombok.Builder;
import lombok.Data;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;

@Data
@Builder
public class FormattedCertificate {

  private Certificate metadata;
  private String formattedContent;
}
