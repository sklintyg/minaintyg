package se.inera.intyg.minaintyg.integration.intygstjanst;

import java.time.LocalDateTime;
import java.util.Optional;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

public class CertificateStatusFactory {

    public static final int DAYS_LIMIT_FOR_STATUS_NEW = 14;

    private CertificateStatusFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static Optional<CertificateStatusType> replaced(CertificateRelationDTO relation) {
        if (relation == null) {
            return Optional.empty();
        }

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
        return issued != null && issued.isAfter(LocalDateTime.now().minusDays(DAYS_LIMIT_FOR_STATUS_NEW).minusDays(1))
            ? Optional.of(CertificateStatusType.NEW)
            : Optional.empty();
    }

}
