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

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateListIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateListIntegrationService;
import se.inera.intyg.minaintyg.logging.service.MonitoringLogService;
import se.inera.intyg.minaintyg.user.UserService;

@Service
@RequiredArgsConstructor
public class ListCertificatesService {

  private final GetCertificateListIntegrationService getCertificateListIntegrationService;
  private final UserService userService;
  private final MonitoringLogService monitoringLogService;

  public ListCertificatesResponse get(ListCertificatesRequest request) {
    final var user = userService.getLoggedInUser().orElseThrow();

    final var response =
        getCertificateListIntegrationService.get(
            GetCertificateListIntegrationRequest.builder()
                .patientId(user.getPersonId())
                .years(request.getYears())
                .units(request.getUnits())
                .statuses(request.getStatuses())
                .certificateTypes(request.getCertificateTypes())
                .build());

    monitoringLogService.logListCertificates(user.getPersonId(), response.getContent().size());

    return ListCertificatesResponse.builder().content(response.getContent()).build();
  }
}
