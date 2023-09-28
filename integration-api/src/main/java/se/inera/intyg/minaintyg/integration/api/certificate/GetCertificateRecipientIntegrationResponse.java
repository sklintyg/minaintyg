package se.inera.intyg.minaintyg.integration.api.certificate;

import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRecipient;

@Value
@Builder
public class GetCertificateRecipientIntegrationResponse {

  CertificateRecipient certificateRecipient;
}
