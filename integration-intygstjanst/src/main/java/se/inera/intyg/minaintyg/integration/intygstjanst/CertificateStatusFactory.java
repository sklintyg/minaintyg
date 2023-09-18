package se.inera.intyg.minaintyg.integration.intygstjanst;

import java.time.LocalDateTime;
import java.util.Optional;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

public class CertificateStatusFactory {

  public static Optional<CertificateStatusType> replaced(CertificateRelationDTO relation) {
    return relation.getType() == CertificateRelationType.REPLACED
        ? Optional.of(CertificateStatusType.REPLACED)
        : Optional.empty();
  }

  public static Optional<CertificateStatusType> sent(CertificateRecipientDTO recipient) {
    if (recipient == null) {
      return Optional.empty();
    }

    return recipient.getSent() == null
        ? Optional.of(CertificateStatusType.NOT_SENT)
        : Optional.of(CertificateStatusType.SENT);

  }

  public static Optional<CertificateStatusType> newStatus(LocalDateTime issued) {
    return issued != null && issued.isAfter(LocalDateTime.now().minusMonths(1))
        ? Optional.of(CertificateStatusType.NEW)
        : Optional.empty();
  }

}
