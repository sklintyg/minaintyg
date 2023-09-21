package se.inera.intyg.minaintyg.integration.api.certificate;

import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateResponse;

public interface GetCompleteCertificateService {

  CertificateResponse get(CertificateRequest request);
}