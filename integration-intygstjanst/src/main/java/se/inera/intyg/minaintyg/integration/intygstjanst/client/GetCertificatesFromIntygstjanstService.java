package se.inera.intyg.minaintyg.integration.intygstjanst.client;

import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificatesRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificatesResponse;

public interface GetCertificatesFromIntygstjanstService {
    CertificatesResponse get(CertificatesRequest request);
}
