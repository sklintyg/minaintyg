package se.inera.intyg.minaintyg.certificate.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateListItem;

@Value
@Builder
public class CertificateListResponseDTO {

  List<CertificateListItem> content;
}
