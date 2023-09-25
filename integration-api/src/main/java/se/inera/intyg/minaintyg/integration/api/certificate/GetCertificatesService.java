package se.inera.intyg.minaintyg.integration.api.certificate;

import se.inera.intyg.minaintyg.integration.api.certificate.model.list.CertificateListRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.model.list.CertificateListResponse;

public interface GetCertificatesService {

  CertificateListResponse get(CertificateListRequest request);
}