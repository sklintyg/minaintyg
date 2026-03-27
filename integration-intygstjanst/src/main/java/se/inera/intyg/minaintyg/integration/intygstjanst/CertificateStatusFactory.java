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

import static se.inera.intyg.minaintyg.integration.api.certificate.CertificateConstants.DAYS_LIMIT_FOR_STATUS_NEW;

import java.time.LocalDateTime;
import java.util.Optional;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

public class CertificateStatusFactory {

  private CertificateStatusFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static Optional<CertificateStatusType> replaced(CertificateRelationDTO relation) {
    if (relation == null) {
      return Optional.empty();
    }

    return relation.getType() == CertificateRelationType.REPLACED
        ? Optional.of(CertificateStatusType.REPLACED)
        : Optional.empty();
  }

  public static Optional<CertificateStatusType> sent(CertificateRecipientDTO recipient) {
    if (recipient == null) {
      return Optional.empty();
    }

    return recipient.getSent() == null
        ? Optional.of(CertificateStatusType.NOT_SENT)
        : Optional.of(CertificateStatusType.SENT);
  }

  public static Optional<CertificateStatusType> newStatus(LocalDateTime issued) {
    return issued != null
            && issued.isAfter(LocalDateTime.now().minusDays(DAYS_LIMIT_FOR_STATUS_NEW).minusDays(1))
        ? Optional.of(CertificateStatusType.NEW)
        : Optional.empty();
  }
}
