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

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateEvent;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

@Service
public class CertificateEventService {

  public List<CertificateEvent> get(
      List<CertificateRelationDTO> relations, CertificateRecipientDTO recipient) {

    final var events =
        Stream.concat(
                getEventsFromRelations(relations).stream(),
                Stream.of(CertificateEventFactory.sent(recipient)))
            .toList();

    return events.stream().filter(Optional::isPresent).map(Optional::get).toList();
  }

  private List<Optional<CertificateEvent>> getEventsFromRelations(
      List<CertificateRelationDTO> relations) {
    return relations.stream().map(this::getEvent).toList();
  }

  private Optional<CertificateEvent> getEvent(CertificateRelationDTO relation) {
    if (relation.getType() == CertificateRelationType.REPLACED) {
      return CertificateEventFactory.replaced(relation);
    }

    if (relation.getType() == CertificateRelationType.REPLACES) {
      return CertificateEventFactory.replaces(relation);
    }

    return Optional.empty();
  }
}
