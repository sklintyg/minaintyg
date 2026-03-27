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

import java.util.Optional;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateEvent;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

public class CertificateEventFactory {

  private CertificateEventFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static Optional<CertificateEvent> sent(CertificateRecipientDTO recipient) {
    if (recipient == null || recipient.getSent() == null) {
      return Optional.empty();
    }

    return Optional.of(
        CertificateEvent.builder()
            .timestamp(recipient.getSent())
            .description("Skickat till " + recipient.getName())
            .build());
  }

  public static Optional<CertificateEvent> replaced(CertificateRelationDTO relation) {
    return event(
        relation, CertificateRelationType.REPLACED, "Ersattes av vården med ett nytt intyg");
  }

  public static Optional<CertificateEvent> replaces(CertificateRelationDTO relation) {
    return event(
        relation,
        CertificateRelationType.REPLACES,
        "Ersätter ett intyg som inte längre är aktuellt");
  }

  private static Optional<CertificateEvent> event(
      CertificateRelationDTO relation, CertificateRelationType expectedType, String description) {
    if (relation == null || relation.getType() != expectedType) {
      return Optional.empty();
    }

    return event(relation, description);
  }

  private static Optional<CertificateEvent> event(
      CertificateRelationDTO relation, String description) {
    if (relation == null) {
      return Optional.empty();
    }

    return Optional.of(
        CertificateEvent.builder()
            .timestamp(relation.getTimestamp())
            .certificateId(relation.getCertificateId())
            .description(description)
            .build());
  }
}
