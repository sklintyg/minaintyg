/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
public class FormattedCertificateTextConverter {

  public String convert(CertificateText certificateText) {
    if (certificateText.getLinks() == null || certificateText.getLinks().isEmpty()) {
      return certificateText.getText();
    }

    final var linkIds = getLinkIds(certificateText);
    final var formattedLinks = getFormattedLinks(certificateText);

    return StringUtils.replaceEach(certificateText.getText(), linkIds, formattedLinks);
  }

  private String[] getFormattedLinks(CertificateText certificateText) {
    return toArray(certificateText.getLinks(), this::formatLink);
  }

  private String[] getLinkIds(CertificateText text) {
    return toArray(text.getLinks(), this::formatLinkId);
  }

  private String[] toArray(
      List<CertificateLink> links, Function<CertificateLink, String> formatter) {
    return links.stream().map(formatter).toArray(String[]::new);
  }

  private String formatLink(CertificateLink link) {
    return HTMLTextFactory.link(link.getUrl(), link.getName());
  }

  private String formatLinkId(CertificateLink link) {
    return "{" + link.getId() + "}";
  }
}
