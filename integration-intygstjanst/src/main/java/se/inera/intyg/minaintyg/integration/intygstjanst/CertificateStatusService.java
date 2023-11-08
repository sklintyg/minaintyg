package se.inera.intyg.minaintyg.integration.intygstjanst;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

@Service
public class CertificateStatusService {

  public List<CertificateStatusType> get(List<CertificateRelationDTO> relations,
      CertificateRecipientDTO recipient,
      LocalDateTime issued) {

    final var statuses = getReplacedStatusFromRelations(relations);

    if (notReplaced(statuses)) {
      statuses.add(CertificateStatusFactory.sent(recipient));
      statuses.add(CertificateStatusFactory.newStatus(issued));
    }

    return statuses
        .stream()
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }

  private List<Optional<CertificateStatusType>> getReplacedStatusFromRelations(
      List<CertificateRelationDTO> relations) {
    return relations
        .stream()
        .filter(relation -> relation.getType() == CertificateRelationType.REPLACED)
        .map(CertificateStatusFactory::replaced)
        .collect(Collectors.toList());
  }

  private static boolean notReplaced(List<Optional<CertificateStatusType>> statuses) {
    return statuses.stream()
        .filter(Optional::isPresent)
        .map(Optional::get)
        .noneMatch(relation -> relation == CertificateStatusType.REPLACED);
  }
}
