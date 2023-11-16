package se.inera.intyg.minaintyg.certificate.service.dto;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.AvailableFunction;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateTextType;

@Value
@Builder
public class GetCertificateResponse {

  FormattedCertificate certificate;
  List<AvailableFunction> availableFunctions;
  Map<CertificateTextType, String> texts;
}
