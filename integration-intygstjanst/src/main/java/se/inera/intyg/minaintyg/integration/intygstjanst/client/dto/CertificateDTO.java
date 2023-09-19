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

  private CertificateIssuerDTO issuer;
  private CertificateUnitDTO unit;
  private List<CertificateRelationDTO> relations;
  private CertificateRecipientDTO recipient;
  private LocalDateTime issued;
  private String id;
  private CertificateTypeDTO type;
  private CertificateSummaryDTO summary;
}
