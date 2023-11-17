package se.inera.intyg.minaintyg.integration.webcert.converter.text;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateLink;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateTextDTO;

@Component
@RequiredArgsConstructor
public class CertificateTextConverter {

  private final CertificateLinkConverter certificateLinkConverter;

  public CertificateText convert(CertificateTextDTO certificateText) {
    return CertificateText.builder()
        .text(certificateText.getText())
        .type(certificateText.getType())
        .links(
            convertLinks(certificateText)
        )
        .build();
  }

  private List<CertificateLink> convertLinks(CertificateTextDTO text) {
    return text.getLinks() == null ? Collections.emptyList()
        : text.getLinks().stream()
            .map(certificateLinkConverter::convert)
            .toList();
  }
}
