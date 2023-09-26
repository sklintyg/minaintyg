package se.inera.intyg.minaintyg.certificate.legacy;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetCertificateResponse {

  Certificate certificate;
}
