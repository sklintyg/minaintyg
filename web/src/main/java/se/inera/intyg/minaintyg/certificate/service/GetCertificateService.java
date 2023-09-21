package se.inera.intyg.minaintyg.certificate.service;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCompleteCertificateService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateRequest;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;
import se.inera.intyg.minaintyg.user.UserService;

@Service
@RequiredArgsConstructor
public class GetCertificateService {

  private final GetCompleteCertificateService getCompleteCertificateService;
  private final MonitoringLogService monitoringLogService;
  private final UserService userService;
  private final CertificateValueToHTMLConverter certificateValueToHTMLConverter;

  // TODO: Create new converter

  public GetCertificateResponse get(GetCertificateRequest request) {
    final var user = userService.getLoggedInUser().orElseThrow();

    final var response = getCompleteCertificateService.get(
        CertificateRequest
            .builder()
            .certificateId(request.getCertificateId())
            .build()
    );

    monitoringLogService.logGetCertificate(user.getPersonId(), request.getCertificateId());

    return GetCertificateResponse.builder()
        .content(
            response.getCertificate()
                .getCategories()
                .stream()
                .map(certificateValueToHTMLConverter::convert)
                .collect(Collectors.joining())
        )
        .metadata(response.getCertificate().getMetadata())
        .build();

  }
}
