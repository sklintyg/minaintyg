package se.inera.intyg.minaintyg.certificate.service.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.Certificate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListCertificatesResponse {

  List<Certificate> content;
}
