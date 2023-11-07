package se.inera.intyg.minaintyg.certificate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.PrintCertificateRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.PrintCertificateResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationService;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;

@Service
@RequiredArgsConstructor
public class PrintCertificateService {

  private final MonitoringLogService monitoringLogService;
  private final PrintCertificateIntegrationService printCertificateIntegrationService;

  public PrintCertificateResponse print(PrintCertificateRequest request) {
    final var response = printCertificateIntegrationService.print(
        PrintCertificateIntegrationRequest
            .builder()
            .certificateId(request.getCertificateId())
            .customizationId(request.getCustomizationId())
            .build()
    );

    monitoringLogService.logCertificatePrinted(request.getCertificateId());

    return PrintCertificateResponse
        .builder()
        .filename(response.getFilename())
        .pdfData(response.getPdfData())
        .build();
  }
}
