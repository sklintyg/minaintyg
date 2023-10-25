package se.inera.intyg.minaintyg.integration.webcert.converter.metadata;

import static se.inera.intyg.minaintyg.integration.api.certificate.CertificateConstants.DAYS_LIMIT_FOR_STATUS_NEW;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelation;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateStatus;

@Component
public class StatusConverter {

  public List<CertificateStatusType> convert(CertificateMetadataDTO metadataDTO) {
    final var events = new ArrayList<CertificateStatusType>();

    if (isReplaced(metadataDTO)) {
      events.add(CertificateStatusType.REPLACED);
      return events;
    }

    if (isNew(metadataDTO)) {
      events.add(CertificateStatusType.NEW);
    }

    if (notReplaced(metadataDTO) && isSent(metadataDTO)) {
      events.add(CertificateStatusType.SENT);
    }

    if (notReplaced(metadataDTO) && notSent(metadataDTO)) {
      events.add(CertificateStatusType.NOT_SENT);
    }

    return events;
  }

  private static boolean notReplaced(CertificateMetadataDTO metadataDTO) {
    return !isReplaced(metadataDTO);
  }

  private static boolean notSent(CertificateMetadataDTO metadataDTO) {
    return canBeSent(metadataDTO) && !isSentToRecipient(metadataDTO);
  }

  private static boolean isSent(CertificateMetadataDTO metadataDTO) {
    return canBeSent(metadataDTO) && isSentToRecipient(metadataDTO);
  }

  private static boolean isNew(CertificateMetadataDTO metadataDTO) {
    final var createdDate = metadataDTO.getCreated();

    return LocalDateTime.now().minusDays(DAYS_LIMIT_FOR_STATUS_NEW).minusDays(1)
        .isBefore(createdDate);
  }

  private static boolean isSentToRecipient(CertificateMetadataDTO metadataDTO) {
    return metadataDTO.getRecipient().getSent() != null;
  }

  private static boolean isReplaced(CertificateMetadataDTO metadataDTO) {
    if (noChildRelations(metadataDTO)) {
      return false;
    }

    final var isReplaced = Stream.of(metadataDTO.getRelations().getChildren())
        .anyMatch(isReplacedCertificate());
    final var isReplacementSigned = Stream.of(metadataDTO.getRelations().getChildren())
        .anyMatch(child -> CertificateStatus.SIGNED.equals(child.getStatus()));

    return isReplaced && isReplacementSigned;
  }

  private static boolean noChildRelations(CertificateMetadataDTO metadataDTO) {
    return metadataDTO.getRelations() == null
        || metadataDTO.getRelations().getChildren() == null;
  }

  private static Predicate<CertificateRelation> isReplacedCertificate() {
    return child ->
        child.getType() == CertificateRelationType.REPLACED
            || child.getType() == CertificateRelationType.COMPLEMENTED;
  }

  private static boolean canBeSent(CertificateMetadataDTO metadataDTO) {
    return metadataDTO.getRecipient() != null && metadataDTO.getRecipient().getId() != null;
  }
}
