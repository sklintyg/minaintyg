package se.inera.intyg.minaintyg.integration.api.certificate;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SendCertificateIntegrationResponse {

  LocalDateTime sent;
}
