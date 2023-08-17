package se.inera.intyg.minaintyg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.dto.HelloResponseDTO;
import se.inera.intyg.minaintyg.integration.api.CertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.CertificateRequest;

@Service
@RequiredArgsConstructor
public class HelloService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final CertificateIntegrationService certificateIntegrationService;

    public HelloResponseDTO hello() {
        final var certificateResponse = certificateIntegrationService.hello(
            CertificateRequest.builder()
                .includeMessage(true)
                .build()
        );
        return HelloResponseDTO.builder()
            .message(certificateResponse.getMessage())
            .build();
    }
}
