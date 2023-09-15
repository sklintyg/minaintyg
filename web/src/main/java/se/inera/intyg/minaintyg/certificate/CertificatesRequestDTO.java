package se.inera.intyg.minaintyg.certificate;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateStatusType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificatesRequestDTO {

  @Builder.Default
  List<String> years = Collections.emptyList();
  @Builder.Default
  List<String> units = Collections.emptyList();
  @Builder.Default
  List<String> certificateTypes = Collections.emptyList();
  @Builder.Default
  List<CertificateStatusType> statuses = Collections.emptyList();
}
