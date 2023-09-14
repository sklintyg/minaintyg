package se.inera.intyg.minaintyg.certificate.service;

import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesResponse;

public interface ListCertificatesService {
    ListCertificatesResponse get(ListCertificatesRequest request);
}
