package se.inera.intyg.minaintyg.integration.intygstjanst;

import java.util.Optional;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateEvent;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

public class CertificateEventFactory {

  public static Optional<CertificateEvent> sent(CertificateRecipientDTO recipient) {
    if (recipient == null || recipient.getSent() == null) {
      return Optional.empty();
    }

    return Optional.of(CertificateEvent
        .builder()
        .timestamp(recipient.getSent().toString())
        .description("Skickat till " + recipient.getName())
        .build());
  }

  public static Optional<CertificateEvent> renewed(CertificateRelationDTO relation) {
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

  public static Optional<CertificateEvent> renews(CertificateRelationDTO relation) {
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
