package se.inera.intyg.minaintyg.integration.api.certificate;

import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificatesResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificatesRequest;


public interface GetCertificatesService {
    CertificatesResponse get(CertificatesRequest request);
}
