package se.inera.intyg.minaintyg.integration.intygstjanst.client.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDTO {

  String id;
  CertificateTypeDTO type;
  CertificateSummaryDTO summary;
  CertificateIssuerDTO issuer;
  CertificateUnitDTO unit;
  List<CertificateRelationDTO> relations;
  CertificateRecipientDTO recipient;
  LocalDateTime issued;
}
