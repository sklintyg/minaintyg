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
package se.inera.intyg.minaintyg.integration.webcert.converter.metadata;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateIssuer;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateSummary;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateUnit;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.Unit;

@Component
@RequiredArgsConstructor
public class MetadataConverter {

  private final EventConverter eventConverter;
  private final StatusConverter statusConverter;

  public CertificateMetadata convert(CertificateMetadataDTO metadataDTO) {
    return CertificateMetadata.builder()
        .id(metadataDTO.getId())
        .type(convertType(metadataDTO))
        .issuer(convertIssuer(metadataDTO))
        .unit(convertUnit(metadataDTO.getUnit()))
        .careUnit(convertUnit(metadataDTO.getCareUnit()))
        .careProvider(convertUnit(metadataDTO.getCareProvider()))
        .events(eventConverter.convert(metadataDTO))
        .statuses(statusConverter.convert(metadataDTO))
        .issued(metadataDTO.getCreated())
        .recipient(convertRecipient(metadataDTO))
        .summary(convertSummary(metadataDTO))
        .build();
  }

  private CertificateType convertType(CertificateMetadataDTO metadataDTO) {
    return CertificateType.builder()
        .id(metadataDTO.getType())
        .name(metadataDTO.getName())
        .version(metadataDTO.getTypeVersion())
        .build();
  }

  private CertificateIssuer convertIssuer(CertificateMetadataDTO metadataDTO) {
    return CertificateIssuer.builder().name(metadataDTO.getIssuedBy().getFullName()).build();
  }

  private CertificateUnit convertUnit(Unit unit) {
    return CertificateUnit.builder()
        .id(unit.getUnitId())
        .name(unit.getUnitName())
        .address(unit.getAddress())
        .zipCode(unit.getZipCode())
        .city(unit.getCity())
        .phoneNumber(unit.getPhoneNumber())
        .email(unit.getEmail())
        .build();
  }

  private CertificateRecipient convertRecipient(CertificateMetadataDTO metadataDTO) {
    if (recipientIsNull(metadataDTO) || certificateIsReplaced(metadataDTO)) {
      return null;
    }
    return CertificateRecipient.builder()
        .id(metadataDTO.getRecipient().getId())
        .name(metadataDTO.getRecipient().getName())
        .sent(metadataDTO.getRecipient().getSent())
        .build();
  }

  private CertificateSummary convertSummary(CertificateMetadataDTO metadataDTO) {
    if (summaryIsNull(metadataDTO)) {
      return CertificateSummary.builder().build();
    }

    return CertificateSummary.builder()
        .label(metadataDTO.getSummary().getLabel())
        .value(metadataDTO.getSummary().getValue())
        .build();
  }

  private static boolean summaryIsNull(CertificateMetadataDTO metadataDTO) {
    return metadataDTO.getSummary() == null;
  }

  private static boolean recipientIsNull(CertificateMetadataDTO metadataDTO) {
    return metadataDTO.getRecipient() == null;
  }

  private boolean certificateIsReplaced(CertificateMetadataDTO metadataDTO) {
    return statusConverter.convert(metadataDTO).contains(CertificateStatusType.REPLACED);
  }
}
