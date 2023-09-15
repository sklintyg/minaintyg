package se.inera.intyg.minaintyg.integration.intygstjanst.client.dto;

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

  String timestamp; // TODO: Should this be localdatetime?
  String certificateId;
  CertificateRelationType type;
}
