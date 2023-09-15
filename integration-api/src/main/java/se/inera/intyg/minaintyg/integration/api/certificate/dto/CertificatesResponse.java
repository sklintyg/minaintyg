package se.inera.intyg.minaintyg.integration.api.certificate.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificatesResponse {

  List<Certificate> content;
}
