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
package se.inera.intyg.minaintyg.certificate.service;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateText;
import se.inera.intyg.minaintyg.logging.service.MonitoringLogService;
import se.inera.intyg.minaintyg.user.UserService;

@Service
@RequiredArgsConstructor
public class GetCertificateService {

  private final GetCertificateIntegrationService getCertificateIntegrationService;
  private final MonitoringLogService monitoringLogService;
  private final FormattedCertificateConverter formattedCertificateConverter;
  private final FormattedCertificateTextConverter formattedCertificateTextConverter;
  private final UserService userService;

  public GetCertificateResponse get(GetCertificateRequest request) {
    final var loggedInUser = userService.getLoggedInUser().orElseThrow();
    final var response =
        getCertificateIntegrationService.get(
            GetCertificateIntegrationRequest.builder()
                .certificateId(request.getCertificateId())
                .personId(loggedInUser.getPersonId())
                .build());

    monitoringLogService.logCertificateRead(
        request.getCertificateId(), response.getCertificate().getMetadata().getType().getId());

    return GetCertificateResponse.builder()
        .certificate(formattedCertificateConverter.convert(response.getCertificate()))
        .availableFunctions(response.getAvailableFunctions())
        .texts(
            response.getTexts().stream()
                .collect(
                    Collectors.toMap(
                        CertificateText::getType, formattedCertificateTextConverter::convert)))
        .build();
  }
}
