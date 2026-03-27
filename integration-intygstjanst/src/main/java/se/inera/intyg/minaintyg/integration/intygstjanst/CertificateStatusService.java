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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

@Service
public class CertificateStatusService {

  public List<CertificateStatusType> get(
      List<CertificateRelationDTO> relations,
      CertificateRecipientDTO recipient,
      LocalDateTime issued) {
    final var statuses = new ArrayList<>(getReplacedStatusFromRelations(relations));

    if (notReplaced(statuses)) {
      statuses.add(CertificateStatusFactory.sent(recipient));
      statuses.add(CertificateStatusFactory.newStatus(issued));
    }

    return statuses.stream().filter(Optional::isPresent).map(Optional::get).toList();
  }

  private List<Optional<CertificateStatusType>> getReplacedStatusFromRelations(
      List<CertificateRelationDTO> relations) {
    return relations.stream()
        .filter(relation -> relation.getType() == CertificateRelationType.REPLACED)
        .map(CertificateStatusFactory::replaced)
        .toList();
  }

  private static boolean notReplaced(List<Optional<CertificateStatusType>> statuses) {
    return statuses.stream()
        .filter(Optional::isPresent)
        .map(Optional::get)
        .noneMatch(relation -> relation == CertificateStatusType.REPLACED);
  }
}
