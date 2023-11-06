package se.inera.intyg.minaintyg.certificate.service.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.ResourceLink;

@Value
@Builder
public class GetCertificateResponse {

  FormattedCertificate certificate;
  List<ResourceLink> resourceLinks;
}
