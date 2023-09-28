package se.inera.intyg.minaintyg.certificate.service.dto;

import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRecipient;

@Value
@Builder
public class GetCertificateRecipientResponse {

  CertificateRecipient certificateRecipient;
}
