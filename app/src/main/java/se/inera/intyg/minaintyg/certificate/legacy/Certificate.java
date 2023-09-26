package se.inera.intyg.minaintyg.certificate.legacy;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Certificate {

  CertificateMetadata metadata;
  List<CertificateCategory> categories;
}
