package se.inera.intyg.minaintyg.certificate.service;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRequest;
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
    private final GetCertificateService getCertificateService;

    public PrintCertificateResponse print(PrintCertificateRequest request) {
        final var isModifiedPrintRequest = !Strings.isNullOrEmpty(request.getCustomizationId());
        final var certificate = getCertificateService.get(GetCertificateRequest
            .builder()
            .certificateId(request.getCertificateId())
            .build());

        final var response = printCertificateIntegrationService.print(
            PrintCertificateIntegrationRequest
                .builder()
                .certificateId(request.getCertificateId())
                .customizationId(request.getCustomizationId())
                .build()
        );

        monitoringLogService.logCertificatePrinted(
            request.getCertificateId(),
            certificate.getCertificate().getMetadata().getType().getId(),
            isModifiedPrintRequest
        );

        return PrintCertificateResponse
            .builder()
            .filename(response.getFilename())
            .pdfData(response.getPdfData())
            .build();
    }
}
