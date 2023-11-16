package se.inera.intyg.minaintyg.integration.api.certificate.model;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateTextType;

@Value
@Builder
public class CertificateText {

  String text;
  CertificateTextType type;
  List<CertificateLink> links;

}
