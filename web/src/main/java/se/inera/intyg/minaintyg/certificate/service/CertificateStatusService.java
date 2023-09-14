package se.inera.intyg.minaintyg.certificate.service;

import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateRelation;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateStatusType;

import java.time.LocalDateTime;
import java.util.List;

public interface CertificateStatusService {
    List<CertificateStatusType> all(List<CertificateRelation> relations,
                                    CertificateRecipient recipient,
                                    LocalDateTime issued);

}
