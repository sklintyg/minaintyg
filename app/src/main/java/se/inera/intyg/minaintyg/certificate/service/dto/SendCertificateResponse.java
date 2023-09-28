package se.inera.intyg.minaintyg.certificate.service.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SendCertificateResponse {

  LocalDateTime sent;
}
