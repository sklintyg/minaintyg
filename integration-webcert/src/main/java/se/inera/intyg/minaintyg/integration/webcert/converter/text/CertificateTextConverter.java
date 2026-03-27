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
        .links(convertLinks(certificateText))
        .build();
  }

  private List<CertificateLink> convertLinks(CertificateTextDTO text) {
    return text.getLinks() == null
        ? Collections.emptyList()
        : text.getLinks().stream().map(certificateLinkConverter::convert).toList();
  }
}
