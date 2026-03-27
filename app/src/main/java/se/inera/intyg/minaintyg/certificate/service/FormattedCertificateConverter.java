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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.FormattedCertificate;
import se.inera.intyg.minaintyg.certificate.service.dto.FormattedCertificateCategory;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;

@Service
@RequiredArgsConstructor
public class FormattedCertificateConverter {

  private final FormattedCategoryConverter formattedCategoryConverter;

  public FormattedCertificate convert(Certificate certificate) {
    return FormattedCertificate.builder()
        .metadata(certificate.getMetadata())
        .content(convertCategories(certificate.getCategories()))
        .build();
  }

  private List<FormattedCertificateCategory> convertCategories(
      List<CertificateCategory> categories) {
    return categories.stream().map(formattedCategoryConverter::convert).toList();
  }
}
