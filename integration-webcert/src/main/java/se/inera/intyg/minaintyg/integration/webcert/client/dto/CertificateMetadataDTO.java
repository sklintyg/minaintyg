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
package se.inera.intyg.minaintyg.integration.webcert.client.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateConfirmationModal;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelations;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateStatus;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateSummary;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.Patient;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.Staff;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.Unit;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificateMetadataDTO {

  private String id;
  private String type;
  private String typeVersion;
  private String typeName;
  private String name;
  private String description;
  private LocalDateTime created;
  private CertificateStatus status;
  private boolean testCertificate;
  private boolean forwarded;
  private boolean sent;
  private boolean availableForCitizen;
  private String sentTo;
  private CertificateRelations relations;
  private Unit unit;
  private Unit careUnit;
  private Unit careProvider;
  private Patient patient;
  private Staff issuedBy;
  private long version;
  private boolean latestMajorVersion;
  private LocalDateTime readyForSign;
  private LocalDateTime signed;
  private LocalDateTime modified;
  private String responsibleHospName;
  private CertificateRecipient recipient;
  private CertificateSummary summary;
  private CertificateConfirmationModal confirmationModal;
  private CertificateConfirmationModal signConfirmationModal;
  private boolean validForSign;
  private String externalReference;
  private List<CertificateMessageType> messageTypes;
}
