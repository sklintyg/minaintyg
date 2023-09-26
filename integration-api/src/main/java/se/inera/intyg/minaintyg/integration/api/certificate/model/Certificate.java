package se.inera.intyg.minaintyg.integration.api.certificate.model;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Certificate {

  CertificateMetadata metadata;
  List<CertificateCategory> categories;
}
