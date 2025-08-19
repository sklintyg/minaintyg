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
    final var response = getCertificateIntegrationService.get(
        GetCertificateIntegrationRequest
            .builder()
            .certificateId(request.getCertificateId())
            .personId(loggedInUser.getUserId())
            .build()
    );

    monitoringLogService.logCertificateRead(
        request.getCertificateId(),
        response.getCertificate().getMetadata().getType().getId()
    );

    return GetCertificateResponse.builder()
        .certificate(formattedCertificateConverter.convert(response.getCertificate()))
        .availableFunctions(response.getAvailableFunctions())
        .texts(
            response.getTexts().stream()
                .collect(
                    Collectors.toMap(CertificateText::getType,
                        formattedCertificateTextConverter::convert))
        )
        .build();
  }
}
