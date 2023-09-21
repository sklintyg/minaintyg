package se.inera.intyg.minaintyg.certificate.service.dto;

import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;

@Value
@Builder
public class GetCertificateResponse {

  // TODO: Do we want to have a different format of Certificate instead that includes HTML and metadata

  Certificate metadata;
  String content;
}
