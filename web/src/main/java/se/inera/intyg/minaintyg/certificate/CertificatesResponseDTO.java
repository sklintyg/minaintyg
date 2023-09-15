package se.inera.intyg.minaintyg.certificate;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificatesResponseDTO {

  List<Certificate> content;
}
