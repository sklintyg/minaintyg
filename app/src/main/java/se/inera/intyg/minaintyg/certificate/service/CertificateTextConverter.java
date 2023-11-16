package se.inera.intyg.minaintyg.certificate.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateLink;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateText;
import se.inera.intyg.minaintyg.util.html.HTMLTextFactory;

@Service
@RequiredArgsConstructor
public class CertificateTextConverter {

  public static final String REGEX = "[^}]*+";

  public String convert(CertificateText text) {
    if (hasLinks(text.getText())) {
      return formatText(text);
    }

    return text.getText();
  }

  private boolean hasLinks(String text) {
    return text.split(REGEX).length > 1;
  }

  private String formatText(CertificateText text) {
    final var linkIds = text.getLinks()
        .stream()
        .map(this::formatLinkId)
        .toArray(String[]::new);

    final var formattedLinks = text.getLinks()
        .stream()
        .map(this::formatLink)
        .toArray(String[]::new);

    return StringUtils.replaceEach(text.getText(), linkIds, formattedLinks);
  }

  private String formatLink(CertificateLink link) {
    return HTMLTextFactory.link(link.getUrl(), link.getName());
  }

  private String formatLinkId(CertificateLink link) {
    return "{" + link.getId() + "}";
  }
}
