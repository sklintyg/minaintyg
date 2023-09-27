package se.inera.intyg.minaintyg.certificate.dto;

import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.certificate.service.dto.FormattedCertificate;

@Value
@Builder
public class CertificateResponseDTO {

  FormattedCertificate certificate;
}
