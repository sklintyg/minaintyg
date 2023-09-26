package se.inera.intyg.minaintyg.integration.api.certificate;

import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;

@Value
@Builder
public class GetCertificateIntegrationResponse {

  Certificate certificate;
}
