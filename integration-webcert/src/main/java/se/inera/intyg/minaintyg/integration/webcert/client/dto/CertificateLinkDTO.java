package se.inera.intyg.minaintyg.integration.webcert.client.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificateLinkDTO {

  String id;
  String name;
  String url;
}
