package se.inera.intyg.minaintyg.certificate.service;

import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.CertificateEvent;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateRelation;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificateEventServiceImpl implements CertificateEventService {

    @Override
    public List<CertificateEvent> get(List<CertificateRelation> relations, CertificateRecipient recipient) {
        final var events = relations
                .stream()
                .map(relation -> relation.getType() == CertificateRelationType.RENEWED
                        ? CertificateEventFactory.renewed(relation)
                        : CertificateEventFactory.renews(relation)
                )
                .collect(Collectors.toList());

        events.add(CertificateEventFactory.sent(recipient));

        return events
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
