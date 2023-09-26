package se.inera.intyg.minaintyg.integration.intygstjanst.client.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRelationType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRelationDTO {

  private LocalDateTime timestamp;
  private String certificateId;
  private CertificateRelationType type;
}
