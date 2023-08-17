package se.inera.intyg.minaintyg.integration.webcert;

import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.CertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.CertificateRequest;
import se.inera.intyg.minaintyg.integration.api.CertificateResponse;


@Service
public class WebcertCertificateIntegrationService implements CertificateIntegrationService {

    @Override
    public CertificateResponse hello(CertificateRequest request) {
        return CertificateResponse.builder()
            .message("Hello!")
            .build();
    }
}
