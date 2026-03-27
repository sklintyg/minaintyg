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
package se.inera.intyg.minaintyg.integration.webcert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HexFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.webcert.client.PrintCertificateFromWebcertService;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.PrintCertificateResponseDTO;

@ExtendWith(MockitoExtension.class)
class WebcertPrintCertificateIntegrationServiceTest {

  private static final String FILENAME = "NAME";
  private static final byte[] PDF_DATA =
      HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
  private static final PrintCertificateResponseDTO EXPECTED_RESPONSE =
      PrintCertificateResponseDTO.builder().pdfData(PDF_DATA).filename(FILENAME).build();
  private static final PrintCertificateIntegrationRequest REQUEST =
      PrintCertificateIntegrationRequest.builder()
          .certificateId("ID")
          .customizationId("C_ID")
          .personId("PERSON_ID")
          .build();

  @Mock PrintCertificateFromWebcertService printCertificateFromWebcertService;
  @InjectMocks WebcertPrintCertificateIntegrationService webcertPrintCertificateIntegrationService;

  @Nested
  class ConvertedResponse {

    @BeforeEach
    void setup() {
      when(printCertificateFromWebcertService.print(any(PrintCertificateIntegrationRequest.class)))
          .thenReturn(EXPECTED_RESPONSE);
    }

    @Test
    void shouldReturnFilenameFromIntegrationService() {
      final var response = webcertPrintCertificateIntegrationService.print(REQUEST);

      assertEquals(EXPECTED_RESPONSE.getFilename(), response.getFilename());
    }

    @Test
    void shouldReturnPdfDataFromIntegrationService() {
      final var response = webcertPrintCertificateIntegrationService.print(REQUEST);

      assertEquals(EXPECTED_RESPONSE.getPdfData(), response.getPdfData());
    }
  }

  @Nested
  class ValidateRequest {

    @Test
    void shouldThrowErrorIfRequestIsNull() {
      assertThrows(
          IllegalArgumentException.class,
          () -> webcertPrintCertificateIntegrationService.print(null));
    }

    @Test
    void shouldThrowErrorIfCertificateIdIsNull() {
      final var request =
          PrintCertificateIntegrationRequest.builder().customizationId("id").build();

      assertThrows(
          IllegalArgumentException.class,
          () -> webcertPrintCertificateIntegrationService.print(request));
    }

    @Test
    void shouldThrowErrorIfCertificateIdIsEmpty() {
      final var request =
          PrintCertificateIntegrationRequest.builder()
              .certificateId("")
              .customizationId("id")
              .build();

      assertThrows(
          IllegalArgumentException.class,
          () -> webcertPrintCertificateIntegrationService.print(request));
    }

    @Test
    void shouldThrowErrorIfPersonIdIsNull() {
      final var request =
          PrintCertificateIntegrationRequest.builder()
              .customizationId("id")
              .certificateId("id")
              .build();

      assertThrows(
          IllegalArgumentException.class,
          () -> webcertPrintCertificateIntegrationService.print(request));
    }

    @Test
    void shouldThrowErrorIfPersonIdIsEmpty() {
      final var request =
          PrintCertificateIntegrationRequest.builder()
              .certificateId("id")
              .customizationId("id")
              .personId("")
              .build();

      assertThrows(
          IllegalArgumentException.class,
          () -> webcertPrintCertificateIntegrationService.print(request));
    }
  }

  @Nested
  class ValidateResponse {

    @Test
    void shouldThrowErrorIfResponseIsNull() {
      assertThrows(
          IllegalArgumentException.class,
          () -> webcertPrintCertificateIntegrationService.print(REQUEST));
    }

    @Test
    void shouldThrowErrorIfFilenameIsNull() {
      when(printCertificateFromWebcertService.print(any(PrintCertificateIntegrationRequest.class)))
          .thenReturn(PrintCertificateResponseDTO.builder().pdfData(PDF_DATA).build());

      assertThrows(
          IllegalArgumentException.class,
          () -> webcertPrintCertificateIntegrationService.print(REQUEST));
    }

    @Test
    void shouldThrowErrorIfFilenameIsEmpty() {
      when(printCertificateFromWebcertService.print(any(PrintCertificateIntegrationRequest.class)))
          .thenReturn(PrintCertificateResponseDTO.builder().filename("").pdfData(PDF_DATA).build());

      assertThrows(
          IllegalArgumentException.class,
          () -> webcertPrintCertificateIntegrationService.print(REQUEST));
    }

    @Test
    void shouldThrowErrorIfPdfDataIsNull() {
      when(printCertificateFromWebcertService.print(any(PrintCertificateIntegrationRequest.class)))
          .thenReturn(PrintCertificateResponseDTO.builder().filename(FILENAME).build());

      assertThrows(
          IllegalArgumentException.class,
          () -> webcertPrintCertificateIntegrationService.print(REQUEST));
    }

    @Test
    void shouldThrowErrorIfPdfDataIsEmpty() {
      final var empty = HexFormat.of().parseHex("");
      when(printCertificateFromWebcertService.print(any(PrintCertificateIntegrationRequest.class)))
          .thenReturn(
              PrintCertificateResponseDTO.builder().filename(FILENAME).pdfData(empty).build());

      assertThrows(
          IllegalArgumentException.class,
          () -> webcertPrintCertificateIntegrationService.print(REQUEST));
    }
  }
}
