package se.inera.intyg.minaintyg.integration.webcert.converter.text;

import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateLink;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateLinkDTO;

@Component
public class CertificateLinkConverter {

  public CertificateLink convert(CertificateLinkDTO link) {
    return CertificateLink.builder()
        .id(link.getId())
        .name(link.getName())
        .url(link.getUrl())
        .build();
  }
}
