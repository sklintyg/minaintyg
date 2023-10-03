package se.inera.intyg.minaintyg.certificate.dto;

import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRecipient;

@Value
@Builder
public class GetCertificateRecipientResponseDTO {

  CertificateRecipient recipient;
}
