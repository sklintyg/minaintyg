package se.inera.intyg.minaintyg.certificate.service;

import se.inera.intyg.minaintyg.certificate.service.dto.CertificateDTO;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.Certificate;

public interface CertificateConverter {
    CertificateDTO convert(Certificate certificate);
}
