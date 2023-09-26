package se.inera.intyg.minaintyg.certificate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationService;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;
import se.inera.intyg.minaintyg.user.UserService;

@Service
@RequiredArgsConstructor
public class GetCertificateService {

  private final GetCertificateIntegrationService getCertificateIntegrationService;
  private final MonitoringLogService monitoringLogService;
  private final UserService userService;
  private final FormattedCertificateConverter formattedCertificateConverter;

  public GetCertificateResponse get(
      GetCertificateRequest request) {
    final var user = userService.getLoggedInUser().orElseThrow();

    final var response = getCertificateIntegrationService.get(
        GetCertificateIntegrationRequest
            .builder()
            .certificateId(request.getCertificateId())
            .build()
    );

    monitoringLogService.logGetCertificate(user.getPersonId(), request.getCertificateId());

    return GetCertificateResponse.builder()
        .certificate(formattedCertificateConverter.convert(response.getCertificate()))
        .build();

  }
}
