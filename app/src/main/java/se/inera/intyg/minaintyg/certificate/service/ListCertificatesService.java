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

    final var response = getCertificateListIntegrationService.get(
        GetCertificateListIntegrationRequest
            .builder()
            .patientId(user.getPersonId())
            .years(request.getYears())
            .units(request.getUnits())
            .statuses(request.getStatuses())
            .certificateTypes(request.getCertificateTypes())
            .build()
    );

    monitoringLogService.logListCertificates(user.getPersonId(), response.getContent().size());

    return ListCertificatesResponse.builder()
        .content(response.getContent())
        .build();

  }
}
