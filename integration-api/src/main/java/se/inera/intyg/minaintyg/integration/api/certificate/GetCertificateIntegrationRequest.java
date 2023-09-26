package se.inera.intyg.minaintyg.integration.api.certificate;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetCertificateIntegrationRequest {

  String certificateId;
}
