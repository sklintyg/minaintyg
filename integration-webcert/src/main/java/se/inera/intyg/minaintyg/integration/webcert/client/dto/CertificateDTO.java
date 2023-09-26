package se.inera.intyg.minaintyg.integration.webcert.client.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDTO {

  CertificateMetadata metadata;
  Map<String, CertificateDataElement> data;
}
