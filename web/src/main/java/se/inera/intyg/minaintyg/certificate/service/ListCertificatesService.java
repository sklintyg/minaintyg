package se.inera.intyg.minaintyg.certificate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificatesService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificatesRequest;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;
import se.inera.intyg.minaintyg.user.UserService;

@Service
@RequiredArgsConstructor
public class ListCertificatesService {

  private final GetCertificatesService getCertificatesService;
  private final UserService userService;
  private final MonitoringLogService monitoringLogService;

  public ListCertificatesResponse get(ListCertificatesRequest request) {
    final var user = userService.getLoggedInUser().orElseThrow();

    final var response = getCertificatesService.get(
        CertificatesRequest
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
