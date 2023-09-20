package se.inera.intyg.minaintyg.integration.intygstjanst;

import java.util.Optional;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateEvent;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

public class CertificateEventFactory {

  private CertificateEventFactory() {
    throw new IllegalStateException("Utility class");
  }

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
    return event(relation, CertificateRelationType.REPLACED,
        "Ersattes av vården med ett nytt intyg");
  }

  public static Optional<CertificateEvent> replaces(CertificateRelationDTO relation) {
    return event(relation, CertificateRelationType.REPLACES,
        "Ersätter ett intyg som inte längre är aktuellt");
  }

  private static Optional<CertificateEvent> event(CertificateRelationDTO relation,
      CertificateRelationType expectedType, String description) {
    if (relation == null || relation.getType() != expectedType) {
      return Optional.empty();
    }

    return event(relation, description);
  }

  private static Optional<CertificateEvent> event(CertificateRelationDTO relation,
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
