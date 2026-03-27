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

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.webcert.client.PrintCertificateFromWebcertService;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.PrintCertificateResponseDTO;

@Service
@RequiredArgsConstructor
public class WebcertPrintCertificateIntegrationService
    implements PrintCertificateIntegrationService {

  private final PrintCertificateFromWebcertService printCertificateFromWebcertService;

  @Override
  public PrintCertificateIntegrationResponse print(PrintCertificateIntegrationRequest request) {
    validateRequest(request);

    final var response = printCertificateFromWebcertService.print(request);
    validateResponse(response, request.getCertificateId());

    return PrintCertificateIntegrationResponse.builder()
        .filename(response.getFilename())
        .pdfData(response.getPdfData())
        .build();
  }

  private static void validateResponse(PrintCertificateResponseDTO response, String certificateId) {
    if (response == null || response.getPdfData() == null || response.getPdfData().length == 0) {
      throw new IllegalArgumentException(
          "Printable certificate was not found, certificateId: " + certificateId);
    }

    if (response.getFilename() == null || response.getFilename().isEmpty()) {
      throw new IllegalArgumentException(
          "Filename for pdf was not found, certificateId: " + certificateId);
    }
  }

  private void validateRequest(PrintCertificateIntegrationRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Invalid request was provided, request was null.");
    }

    if (request.getCertificateId() == null || request.getCertificateId().isEmpty()) {
      throw new IllegalArgumentException(
          "Invalid request was provided, certificateId was not defined.");
    }

    if (request.getPersonId() == null || request.getPersonId().isEmpty()) {
      throw new IllegalArgumentException("Invalid request was provided, personId was not defined.");
    }
  }
}
