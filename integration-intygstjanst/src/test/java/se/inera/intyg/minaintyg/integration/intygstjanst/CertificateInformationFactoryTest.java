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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateIssuerDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateSummaryDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateTypeDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateUnitDTO;

class CertificateInformationFactoryTest {

  private static final CertificateDTO CERTIFICATE =
      CertificateDTO.builder()
          .id("ID")
          .issuer(CertificateIssuerDTO.builder().name("issuerName").build())
          .type(
              CertificateTypeDTO.builder().id("typeId").name("typeName").version("version").build())
          .unit(CertificateUnitDTO.builder().id("unitId").name("unitName").build())
          .summary(CertificateSummaryDTO.builder().label("label").value("summary").build())
          .build();

  @Test
  void shouldCreateIssuer() {
    final var response = CertificateInformationFactory.issuer(CERTIFICATE);

    assertEquals(CERTIFICATE.getIssuer().getName(), response.getName());
  }

  @Test
  void shouldCreateTypeId() {
    final var response = CertificateInformationFactory.type(CERTIFICATE);

    assertEquals(CERTIFICATE.getType().getId(), response.getId());
  }

  @Test
  void shouldCreateTypeName() {
    final var response = CertificateInformationFactory.type(CERTIFICATE);

    assertEquals(CERTIFICATE.getType().getName(), response.getName());
  }

  @Test
  void shouldCreateTypeVersion() {
    final var response = CertificateInformationFactory.type(CERTIFICATE);

    assertEquals(CERTIFICATE.getType().getVersion(), response.getVersion());
  }

  @Test
  void shouldCreateSummaryLabel() {
    final var response = CertificateInformationFactory.summary(CERTIFICATE);

    assertEquals(CERTIFICATE.getSummary().getLabel(), response.getLabel());
  }

  @Test
  void shouldCreateSummaryValue() {
    final var response = CertificateInformationFactory.summary(CERTIFICATE);

    assertEquals(CERTIFICATE.getSummary().getValue(), response.getValue());
  }

  @Test
  void shouldCreateUnit() {
    final var response = CertificateInformationFactory.unit(CERTIFICATE);

    assertEquals(CERTIFICATE.getUnit().getId(), response.getId());
  }

  @Test
  void shouldCreateUnitName() {
    final var response = CertificateInformationFactory.unit(CERTIFICATE);

    assertEquals(CERTIFICATE.getUnit().getName(), response.getName());
  }
}
