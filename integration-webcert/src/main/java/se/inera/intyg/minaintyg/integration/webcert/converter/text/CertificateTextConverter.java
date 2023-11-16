package se.inera.intyg.minaintyg.integration.webcert.converter.text;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateTextDTO;

@Component
@RequiredArgsConstructor
public class CertificateTextConverter {

  private final CertificateLinkConverter certificateLinkConverter;

  public CertificateText convert(CertificateTextDTO text) {
    return CertificateText.builder()
        .text(text.getText())
        .type(text.getType())
        .links(
            text.getLinks().stream()
                .map(certificateLinkConverter::convert)
                .toList()
        )
        .build();
  }
}
