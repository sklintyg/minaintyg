package se.inera.intyg.minaintyg.integration.intygstjanst;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

@Service
public class CertificateStatusService {

  public List<CertificateStatusType> get(List<CertificateRelationDTO> relations,
      CertificateRecipientDTO recipient,
      LocalDateTime issued) {

    List<Optional<CertificateStatusType>> statuses;

    if (relations == null) {
      statuses = new ArrayList<>();
    } else {
      statuses = relations
          .stream()
          .map(this::renewed)
          .collect(Collectors.toList());
    }

    statuses.add(sent(recipient));
    statuses.add(newStatus(issued));

    return statuses
        .stream()
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }

  private Optional<CertificateStatusType> renewed(CertificateRelationDTO relation) {
    if (relation.getType() == CertificateRelationType.RENEWED) {
      return Optional.of(CertificateStatusType.RENEWED);
    }

    return Optional.empty();
  }

  private Optional<CertificateStatusType> sent(CertificateRecipientDTO recipient) {
    if (recipient != null) {
      return recipient.getSent() == null
          ? Optional.of(CertificateStatusType.NOT_SENT)
          : Optional.of(CertificateStatusType.SENT);

    }

    return Optional.empty();
  }

  private Optional<CertificateStatusType> newStatus(LocalDateTime issued) {
    return issued != null && issued.isAfter(LocalDateTime.now().minusMonths(1))
        ? Optional.of(CertificateStatusType.NEW) : Optional.empty();
  }
}
