package se.inera.intyg.minaintyg.integration.webcert.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateLinkDTO {

  String id;
  String name;
  String url;
}
