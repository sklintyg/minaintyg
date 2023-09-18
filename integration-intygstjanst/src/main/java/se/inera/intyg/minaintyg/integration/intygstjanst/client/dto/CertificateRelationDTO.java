package se.inera.intyg.minaintyg.integration.intygstjanst.client.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateRelationType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRelationDTO {

  LocalDateTime timestamp;
  String certificateId;
  CertificateRelationType type;
}
