package se.inera.intyg.minaintyg.integration.intygstjanst;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateEvent;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

@Service
public class CertificateEventService {

  public List<CertificateEvent> get(List<CertificateRelationDTO> relations,
      CertificateRecipientDTO recipient) {

    final var events = Stream.concat(
            getEventsFromRelations(relations).stream(),
            Stream.of(CertificateEventFactory.sent(recipient))
        )
        .toList();

    return events.stream()
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }

  private List<Optional<CertificateEvent>> getEventsFromRelations(
      List<CertificateRelationDTO> relations) {
    return relations.stream()
        .map(this::getEvent)
        .toList();
  }

  private Optional<CertificateEvent> getEvent(CertificateRelationDTO relation) {
    if (relation.getType() == CertificateRelationType.REPLACED) {
      return CertificateEventFactory.replaced(relation);
    }

    if (relation.getType() == CertificateRelationType.REPLACES) {
      return CertificateEventFactory.replaces(relation);
    }

    return Optional.empty();
  }
}
