package se.inera.intyg.minaintyg.integration.api.certificate.model.list;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificateType {

  String id;
  String name;
  String version;
}
