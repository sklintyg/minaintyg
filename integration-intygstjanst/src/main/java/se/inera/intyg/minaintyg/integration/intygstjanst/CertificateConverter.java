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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateListItem;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateEvent;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateDTO;

@Service
@RequiredArgsConstructor
public class CertificateConverter {

  private final CertificateEventService certificateEventService;
  private final CertificateStatusService certificateStatusService;

  public CertificateListItem convert(CertificateDTO certificate) {
    return CertificateListItem.builder()
        .id(certificate.getId())
        .type(CertificateInformationFactory.type(certificate))
        .unit(CertificateInformationFactory.unit(certificate))
        .issuer(CertificateInformationFactory.issuer(certificate))
        .summary(CertificateInformationFactory.summary(certificate))
        .issued(certificate.getIssued())
        .statuses(convertStatuses(certificate))
        .events(convertEvents(certificate))
        .build();
  }

  private List<CertificateEvent> convertEvents(CertificateDTO certificate) {
    return certificateEventService.get(certificate.getRelations(), certificate.getRecipient());
  }

  private List<CertificateStatusType> convertStatuses(CertificateDTO certificate) {
    return certificateStatusService.get(
        certificate.getRelations(), certificate.getRecipient(), certificate.getIssued());
  }
}
