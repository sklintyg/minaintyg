package se.inera.intyg.minaintyg.integration.intygstjanst.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateTypeDTO {

  String id;
  String name;
  String version;
}
