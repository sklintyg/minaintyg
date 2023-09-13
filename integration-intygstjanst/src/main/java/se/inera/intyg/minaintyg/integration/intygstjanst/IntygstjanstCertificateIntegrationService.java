package se.inera.intyg.minaintyg.integration.intygstjanst;

import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificatesService;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificatesResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificatesRequest;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.GetCertificatesFromIntygstjanstService;

@Service
public class IntygstjanstCertificateIntegrationService implements GetCertificatesService {

    private final GetCertificatesFromIntygstjanstService getCertificatesFromIntygstjanstService;

    public IntygstjanstCertificateIntegrationService(
            GetCertificatesFromIntygstjanstService getCertificatesFromIntygstjanstService
    ) {
        this.getCertificatesFromIntygstjanstService = getCertificatesFromIntygstjanstService;
    }

    @Override
    public CertificatesResponse get(CertificatesRequest request) {
        validateRequest(request);
        try {
            return getCertificatesFromIntygstjanstService.get(request);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void validateRequest(CertificatesRequest request) {
        if (request == null || request.getPatientId() == null || request.getPatientId().isEmpty()) {
            throw new IllegalArgumentException("Valid request was not provided: " + request);
        }
    }
}