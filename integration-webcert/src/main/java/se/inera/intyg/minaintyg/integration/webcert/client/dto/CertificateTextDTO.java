package se.inera.intyg.minaintyg.integration.webcert.client.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateTextType;

@Value
@Builder
public class CertificateTextDTO {

  String text;
  CertificateTextType type;
  List<CertificateLinkDTO> links;

}
