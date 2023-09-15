package se.inera.intyg.minaintyg.integration.intygstjanst;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateEvent;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

@Service
public class CertificateEventServiceImpl implements CertificateEventService {

  @Override
  public List<CertificateEvent> get(List<CertificateRelationDTO> relations,
      CertificateRecipientDTO recipient) {
    final var events = relations
        .stream()
        .map(relation -> relation.getType() == CertificateRelationType.RENEWED
            ? CertificateEventFactory.renewed(relation)
            : CertificateEventFactory.renews(relation)
        ).collect(Collectors.toList());

    events.add(CertificateEventFactory.sent(recipient));

    return events
        .stream()
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }
}
