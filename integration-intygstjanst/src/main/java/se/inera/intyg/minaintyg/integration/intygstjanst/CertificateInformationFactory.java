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
package se.inera.intyg.minaintyg.integration.intygstjanst;

import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateIssuer;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateSummary;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateUnit;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateDTO;

public class CertificateInformationFactory {

  private CertificateInformationFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static CertificateSummary summary(CertificateDTO certificate) {
    return CertificateSummary.builder()
        .label(certificate.getSummary().getLabel())
        .value(certificate.getSummary().getValue())
        .build();
  }

  public static CertificateIssuer issuer(CertificateDTO certificate) {
    return CertificateIssuer.builder().name(certificate.getIssuer().getName()).build();
  }

  public static CertificateUnit unit(CertificateDTO certificate) {
    return CertificateUnit.builder()
        .name(certificate.getUnit().getName())
        .id(certificate.getUnit().getId())
        .build();
  }

  public static CertificateType type(CertificateDTO certificate) {
    return CertificateType.builder()
        .name(certificate.getType().getName())
        .id(certificate.getType().getId())
        .version(certificate.getType().getVersion())
        .build();
  }
}
