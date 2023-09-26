package se.inera.intyg.minaintyg.certificate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.legacy.GetCertificateRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateResponse;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;
import se.inera.intyg.minaintyg.user.UserService;

@Service
@RequiredArgsConstructor
public class GetCertificateService {

  private final se.inera.intyg.minaintyg.certificate.legacy.GetCertificateService getCertificateService;
  private final MonitoringLogService monitoringLogService;
  private final UserService userService;
  private final CertificateToFormattedCertificateConverter certificateToFormattedCertificateConverter;

  public GetCertificateResponse get(
      se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRequest request) {
    final var user = userService.getLoggedInUser().orElseThrow();

    final var response = getCertificateService.get(
        GetCertificateRequest
            .builder()
            .certificateId(request.getCertificateId())
            .build()
    );

    monitoringLogService.logGetCertificate(user.getPersonId(), request.getCertificateId());

    return GetCertificateResponse.builder()
        .certificate(certificateToFormattedCertificateConverter.convert(response.getCertificate()))
        .build();

  }
}
