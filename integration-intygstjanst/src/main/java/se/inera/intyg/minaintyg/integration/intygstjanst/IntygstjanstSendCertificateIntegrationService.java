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

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.SendCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.SendCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.SendCertificateUsingIntygstjanstService;

@Service
@RequiredArgsConstructor
public class IntygstjanstSendCertificateIntegrationService
    implements SendCertificateIntegrationService {

  private final SendCertificateUsingIntygstjanstService sendCertificateUsingIntygstjanstService;

  @Override
  public void send(SendCertificateIntegrationRequest request) {
    validateRequest(request);
    try {
      sendCertificateUsingIntygstjanstService.send(request);
    } catch (Exception exception) {
      throw new IllegalStateException(exception);
    }
  }

  private void validateRequest(SendCertificateIntegrationRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Valid request was not provided");
    }

    if (request.getCertificateId() == null || request.getCertificateId().isEmpty()) {
      throw new IllegalArgumentException(
          "Valid request was not provided, must include certificate id");
    }

    if (request.getPatientId() == null || request.getPatientId().isEmpty()) {
      throw new IllegalArgumentException("Valid request was not provided, must include patient id");
    }

    if (request.getRecipient() == null || request.getRecipient().isEmpty()) {
      throw new IllegalArgumentException("Valid request was not provided, must include recipient");
    }
  }
}
