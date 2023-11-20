package se.inera.intyg.minaintyg.integration.api.certificate;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateText;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.AvailableFunction;

@Value
@Builder
public class GetCertificateIntegrationResponse {

  Certificate certificate;
  List<AvailableFunction> availableFunctions;
  List<CertificateText> texts;
}
