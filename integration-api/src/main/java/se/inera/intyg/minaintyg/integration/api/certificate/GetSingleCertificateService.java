package se.inera.intyg.minaintyg.integration.api.certificate;

import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateResponse;

public interface GetSingleCertificateService {

  CertificateResponse get(CertificateRequest request);
}