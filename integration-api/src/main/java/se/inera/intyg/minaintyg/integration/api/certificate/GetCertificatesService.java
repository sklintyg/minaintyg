package se.inera.intyg.minaintyg.integration.api.certificate;

import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificatesRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificatesResponse;

public interface GetCertificatesService {

  CertificatesResponse get(CertificatesRequest request);
}