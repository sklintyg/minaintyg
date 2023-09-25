package se.inera.intyg.minaintyg.integration.api.certificate.model.list;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificateListResponse {

  List<CertificateListItem> content;
}
