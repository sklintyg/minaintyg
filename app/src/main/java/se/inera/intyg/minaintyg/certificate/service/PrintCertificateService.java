package se.inera.intyg.minaintyg.certificate.service;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.PrintCertificateRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.PrintCertificateResponse;
import se.inera.intyg.minaintyg.integration.api.analytics.AnalyticsMessageFactory;
import se.inera.intyg.minaintyg.integration.api.analytics.PublishAnalyticsMessage;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationService;
import se.inera.intyg.minaintyg.logging.service.MonitoringLogService;
import se.inera.intyg.minaintyg.user.UserService;

@Service
@RequiredArgsConstructor
public class PrintCertificateService {

  private final MonitoringLogService monitoringLogService;
  private final PrintCertificateIntegrationService printCertificateIntegrationService;
  private final GetCertificateIntegrationService getCertificateIntegrationService;
  private final UserService userService;
  private final AnalyticsMessageFactory analyticsMessageFactory;
  private final PublishAnalyticsMessage publishAnalyticsMessage;

  public PrintCertificateResponse print(PrintCertificateRequest request) {
    final var isModifiedPrintRequest = !Strings.isNullOrEmpty(request.getCustomizationId());
    final var loggedInUser = userService.getLoggedInUser().orElseThrow();
    final var certificate = getCertificateIntegrationService.get(
        GetCertificateIntegrationRequest.builder()
            .certificateId(request.getCertificateId())
            .personId(loggedInUser.getPersonId())
            .build()
    );

    final var response = printCertificateIntegrationService.print(
        PrintCertificateIntegrationRequest
            .builder()
            .certificateId(request.getCertificateId())
            .customizationId(request.getCustomizationId())
            .personId(loggedInUser.getPersonId())
            .build()
    );

    monitoringLogService.logCertificatePrinted(
        request.getCertificateId(),
        certificate.getCertificate().getMetadata().getType().getId(),
        isModifiedPrintRequest
    );

    publishAnalyticsMessage.publishEvent(
        isModifiedPrintRequest ?
            analyticsMessageFactory.certificatePrintedCustomized(certificate.getCertificate()) :
            analyticsMessageFactory.certificatePrinted(certificate.getCertificate())
    );

    return PrintCertificateResponse
        .builder()
        .filename(response.getFilename())
        .pdfData(response.getPdfData())
        .build();
  }
}
