package se.inera.intyg.minaintyg.certificate.service.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetCertificateResponse {

  FormattedCertificate certificate;
}
