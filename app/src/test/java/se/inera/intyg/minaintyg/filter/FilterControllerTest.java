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
package se.inera.intyg.minaintyg.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.certificate.service.GetCertificateListFilterService;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateFilterResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateTypeFilter;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateUnit;

@ExtendWith(MockitoExtension.class)
class FilterControllerTest {

  @Mock GetCertificateListFilterService getCertificateListFilterService;

  @InjectMocks FilterController filterController;
  private final GetCertificateFilterResponse expectedResponse =
      GetCertificateFilterResponse.builder()
          .statuses(List.of(CertificateStatusType.SENT))
          .years(List.of("2020"))
          .certificateTypes(List.of(CertificateTypeFilter.builder().build()))
          .units(List.of(CertificateUnit.builder().build()))
          .total(5)
          .build();

  @BeforeEach
  void setup() {
    when(getCertificateListFilterService.get()).thenReturn(expectedResponse);
  }

  @Nested
  class Response {

    @Test
    void shouldSetCertificateTypes() {
      final var response = filterController.getCertificateListFilter();

      assertEquals(expectedResponse.getCertificateTypes(), response.getCertificateTypes());
    }

    @Test
    void shouldSetYears() {
      final var response = filterController.getCertificateListFilter();

      assertEquals(expectedResponse.getYears(), response.getYears());
    }

    @Test
    void shouldSetUnits() {
      final var response = filterController.getCertificateListFilter();

      assertEquals(expectedResponse.getUnits(), response.getUnits());
    }

    @Test
    void shouldSetStatuses() {
      final var response = filterController.getCertificateListFilter();

      assertEquals(expectedResponse.getStatuses(), response.getStatuses());
    }

    @Test
    void shouldSetTotal() {
      final var response = filterController.getCertificateListFilter();

      assertEquals(expectedResponse.getTotal(), response.getTotal());
    }
  }
}
