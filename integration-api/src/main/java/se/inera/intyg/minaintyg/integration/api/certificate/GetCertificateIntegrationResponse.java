package se.inera.intyg.minaintyg.integration.api.certificate;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.ResourceLink;

@Value
@Builder
public class GetCertificateIntegrationResponse {

  Certificate certificate;
  List<ResourceLink> resourceLinks;
}
