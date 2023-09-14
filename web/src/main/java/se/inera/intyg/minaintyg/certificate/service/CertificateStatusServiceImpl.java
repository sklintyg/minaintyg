package se.inera.intyg.minaintyg.certificate.service;

import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateRelation;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateStatusType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificateStatusServiceImpl implements CertificateStatusService {

    @Override
    public List<CertificateStatusType> all(List<CertificateRelation> relations,
                                           CertificateRecipient recipient,
                                           LocalDateTime issued) {
        final var statuses = relations
                .stream()
                .map(this::renewed)
                .collect(Collectors.toList());

        statuses.add(sent(recipient));
        statuses.add(newStatus(issued));

        return statuses
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<CertificateStatusType> renewed(CertificateRelation relation) {
        if (relation.getType() == CertificateRelationType.RENEWED) {
            return Optional.of(CertificateStatusType.RENEWED);
        }

        return Optional.empty();
    }

    private Optional<CertificateStatusType> sent(CertificateRecipient recipient) {
        if (recipient != null) {
            return recipient.getSent() == null
                    ? Optional.of(CertificateStatusType.NOT_SENT)
                    : Optional.of(CertificateStatusType.SENT);

        }

        return Optional.empty();
    }

    private Optional<CertificateStatusType> newStatus(LocalDateTime issued) {
        return issued.isAfter(LocalDateTime.now().minusMonths(1)) ? Optional.of(CertificateStatusType.NEW) : Optional.empty();
    }
}
