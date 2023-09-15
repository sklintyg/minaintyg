package se.inera.intyg.minaintyg.integration.intygstjanst.client;

import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificatesRequest;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificatesResponseDTO;

public interface GetCertificatesFromIntygstjanstService {

  CertificatesResponseDTO get(CertificatesRequest request);
}
