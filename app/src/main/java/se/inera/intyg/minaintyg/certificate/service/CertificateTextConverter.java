package se.inera.intyg.minaintyg.certificate.service;

import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateLink;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateText;
import se.inera.intyg.minaintyg.util.html.HTMLTextFactory;

@Service
@RequiredArgsConstructor
public class CertificateTextConverter {

  public String convert(CertificateText text) {
    if (text.getLinks() == null || text.getLinks().isEmpty()) {
      return text.getText();
    }

    final var linkIds = getLinkIds(text);
    final var formattedLinks = getFormattedLinks(text);

    return StringUtils.replaceEach(text.getText(), linkIds, formattedLinks);
  }

  private String[] getFormattedLinks(CertificateText text) {
    return toArray(text.getLinks(), this::formatLink);
  }

  private String[] getLinkIds(CertificateText text) {
    return toArray(text.getLinks(), this::formatLinkId);
  }

  private String[] toArray(List<CertificateLink> links,
      Function<CertificateLink, String> formatter) {
    return links.stream()
        .map(formatter)
        .toArray(String[]::new);
  }

  private String formatLink(CertificateLink link) {
    return HTMLTextFactory.link(link.getUrl(), link.getName());
  }

  private String formatLinkId(CertificateLink link) {
    return "{" + link.getId() + "}";
  }
}
