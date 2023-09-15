package se.inera.intyg.minaintyg.integration.intygstjanst;

import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateDTO;

public interface CertificateConverter {

  Certificate convert(CertificateDTO certificate);
}
