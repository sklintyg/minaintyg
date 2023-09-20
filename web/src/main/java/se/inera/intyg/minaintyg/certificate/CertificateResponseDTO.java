package se.inera.intyg.minaintyg.certificate;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificateResponseDTO {

  List<String> values;
}
