package se.inera.intyg.minaintyg.certificate.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SendCertificateResponseDTO {

  LocalDateTime sent;
}
