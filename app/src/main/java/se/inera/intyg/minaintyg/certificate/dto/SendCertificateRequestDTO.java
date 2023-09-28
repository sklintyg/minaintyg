package se.inera.intyg.minaintyg.certificate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendCertificateRequestDTO {

  String certificateId;
  String certificateType;
  String recipient;
}
