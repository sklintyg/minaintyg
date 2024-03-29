package se.inera.intyg.minaintyg.integration.webcert.client.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateTextType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateTextDTO {

  String text;
  CertificateTextType type;
  List<CertificateLinkDTO> links;

}
