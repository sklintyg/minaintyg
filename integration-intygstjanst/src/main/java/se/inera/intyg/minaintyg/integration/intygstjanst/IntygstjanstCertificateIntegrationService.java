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
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateListIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateListIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateListIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateListItem;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.GetCertificatesFromIntygstjanstService;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificatesResponseDTO;

@Service
@RequiredArgsConstructor
public class IntygstjanstCertificateIntegrationService
    implements GetCertificateListIntegrationService {

  private final GetCertificatesFromIntygstjanstService getCertificatesFromIntygstjanstService;
  private final CertificateConverter certificateConverter;

  @Override
  public GetCertificateListIntegrationResponse get(GetCertificateListIntegrationRequest request) {
    validateRequest(request);
    final var response = getCertificatesFromIntygstjanstService.get(request);
    return GetCertificateListIntegrationResponse.builder()
        .content(convertContent(response))
        .build();
  }

  private List<CertificateListItem> convertContent(CertificatesResponseDTO response) {
    return response.getContent().stream().map(certificateConverter::convert).toList();
  }

  private void validateRequest(GetCertificateListIntegrationRequest request) {
    if (request == null || request.getPatientId() == null || request.getPatientId().isEmpty()) {
      throw new IllegalArgumentException("Valid request was not provided, must contain patient id");
    }
  }
}
