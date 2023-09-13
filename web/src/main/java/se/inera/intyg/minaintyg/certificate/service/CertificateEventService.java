package se.inera.intyg.minaintyg.certificate.service;

import se.inera.intyg.minaintyg.certificate.service.dto.CertificateEvent;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateRelation;

import java.util.List;

public interface CertificateEventService {
    List<CertificateEvent> get(List<CertificateRelation> relations, CertificateRecipient recipient);
}
