package se.inera.intyg.minaintyg.certificate.service;

import se.inera.intyg.minaintyg.integration.api.certificate.dto.Certificate;

public interface CertificateConverter {
    se.inera.intyg.minaintyg.certificate.service.dto.Certificate convert(Certificate certificate);
}
