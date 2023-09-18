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
        .timestamp(recipient.getSent())
        .description("Skickat till " + recipient.getName())
        .build());
  }

  public static Optional<CertificateEvent> replaced(CertificateRelationDTO relation) {
    return event(relation, "Ersattes av vården med ett nytt intyg");
  }

  public static Optional<CertificateEvent> replaces(CertificateRelationDTO relation) {
    return event(relation, "Ersätter ett intyg som inte längre är aktuellt");
  }

  public static Optional<CertificateEvent> event(CertificateRelationDTO relation,
      String description) {
    if (relation == null) {
      return Optional.empty();
    }

    return Optional.of(CertificateEvent
        .builder()
        .timestamp(relation.getTimestamp())
        .certificateId(relation.getCertificateId())
        .description(description)
        .build());
  }
}
