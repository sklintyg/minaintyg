package se.inera.intyg.minaintyg.certificate.service;

import se.inera.intyg.minaintyg.certificate.service.dto.CertificateEvent;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateRelation;

import java.util.Optional;

public class CertificateEventFactory {
    public static Optional<CertificateEvent> sent(CertificateRecipient recipient) {
        if (recipient == null || recipient.getSent() == null) {
            return Optional.empty();
        }

        return Optional.of(CertificateEvent
                .builder()
                .timestamp(recipient.getSent().toString())
                .description("Skickat till " + recipient.getName())
                .build());
    }

    public static Optional<CertificateEvent> renewed(CertificateRelation relation) {
        if (relation == null) {
            return Optional.empty();
        }

        return Optional.of(CertificateEvent
                .builder()
                .timestamp(relation.getTimestamp())
                .certificateId(relation.getCertificateId())
                .description("Ersattes av vården med ett nytt intyg")
                .build()
        );
    }

    public static Optional<CertificateEvent> renews(CertificateRelation relation) {
        if (relation == null) {
            return Optional.empty();
        }

        return Optional.of(CertificateEvent
                .builder()
                .timestamp(relation.getTimestamp())
                .certificateId(relation.getCertificateId())
                .description("Ersätter ett intyg som inte längre är aktuellt")
                .build());
    }
}
