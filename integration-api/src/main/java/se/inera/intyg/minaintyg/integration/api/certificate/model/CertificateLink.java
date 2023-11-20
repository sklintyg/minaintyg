package se.inera.intyg.minaintyg.integration.api.certificate.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificateLink {

  String id;
  String name;
  String url;
}
