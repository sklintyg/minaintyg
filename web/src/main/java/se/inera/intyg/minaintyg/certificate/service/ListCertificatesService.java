package se.inera.intyg.minaintyg.certificate.service;

import se.inera.intyg.minaintyg.certificate.service.dto.Certificate;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;

import java.util.List;

public interface ListCertificatesService {
    List<Certificate> get(ListCertificatesRequest request);
}
