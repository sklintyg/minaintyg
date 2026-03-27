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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateListIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateListItem;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.GetCertificatesFromIntygstjanstService;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificatesResponseDTO;

@ExtendWith(MockitoExtension.class)
class IntygstjanstCertificateIntegrationServiceTest {

  private static final String PERSON_ID = "191212121212";
  @Mock private GetCertificatesFromIntygstjanstService getCertificatesFromIntygstjanstService;
  @Mock private CertificateConverter certificateConverter;

  @InjectMocks
  private IntygstjanstCertificateIntegrationService intygstjanstCertificateIntegrationService;

  @Nested
  class TestResponse {

    GetCertificateListIntegrationRequest request;
    CertificatesResponseDTO certificatesResponseDTO;
    CertificateDTO originalCertificate = new CertificateDTO();
    CertificateListItem convertedCertificateListItem;

    @BeforeEach
    void setup() {
      request = GetCertificateListIntegrationRequest.builder().patientId(PERSON_ID).build();

      certificatesResponseDTO =
          CertificatesResponseDTO.builder().content(List.of(originalCertificate)).build();

      when(getCertificatesFromIntygstjanstService.get(request)).thenReturn(certificatesResponseDTO);

      when(certificateConverter.convert(any(CertificateDTO.class)))
          .thenReturn(convertedCertificateListItem);
    }

    @Test
    void shouldCallConverter() {
      intygstjanstCertificateIntegrationService.get(request);

      final var captor = ArgumentCaptor.forClass(CertificateDTO.class);

      verify(certificateConverter).convert(captor.capture());

      assertEquals(originalCertificate, captor.getValue());
    }

    @Test
    void shouldReturnConvertedCertificate() {
      final var result = intygstjanstCertificateIntegrationService.get(request);

      assertEquals(convertedCertificateListItem, result.getContent().get(0));
    }
  }

  @Nested
  class ErrorHandling {

    @Test
    void shouldThrowIllegalArgumentExceptionIfRequestIsNull() {
      assertThrows(
          IllegalArgumentException.class,
          () -> intygstjanstCertificateIntegrationService.get(null));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfRequestContainsNullPatientId() {
      final var request = GetCertificateListIntegrationRequest.builder().patientId(null).build();
      assertThrows(
          IllegalArgumentException.class,
          () -> intygstjanstCertificateIntegrationService.get(request));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfRequestContainsEmptyPatientId() {
      final var request = GetCertificateListIntegrationRequest.builder().patientId("").build();
      assertThrows(
          IllegalArgumentException.class,
          () -> intygstjanstCertificateIntegrationService.get(request));
    }

    @Test
    void shouldReturnStatusErrorIfCommunicationErrorWithITOccurs() {
      final var request =
          GetCertificateListIntegrationRequest.builder().patientId(PERSON_ID).build();
      when(getCertificatesFromIntygstjanstService.get(request)).thenThrow(RuntimeException.class);
      assertThrows(
          RuntimeException.class, () -> intygstjanstCertificateIntegrationService.get(request));
    }
  }
}
