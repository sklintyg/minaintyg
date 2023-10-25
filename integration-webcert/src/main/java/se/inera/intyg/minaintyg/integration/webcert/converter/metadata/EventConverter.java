package se.inera.intyg.minaintyg.integration.webcert.converter.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateEvent;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelation;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateStatus;

@Component
public class EventConverter {

  public static final String EVENT_SENT_TO_DESCRIPTION = "Skickat till ";
  public static final String EVENT_REPLACED = "Ersatt av vården med ett nytt intyg";
  public static final String EVENT_REPLACES = "Ersätter ett intyg som inte längre är aktuellt";

  public List<CertificateEvent> convert(CertificateMetadataDTO metadataDTO) {
    final var events = new ArrayList<CertificateEvent>();

    if (isSent(metadataDTO)) {
      events.add(
          CertificateEvent.builder()
              .timestamp(metadataDTO.getRecipient().getSent())
              .description(EVENT_SENT_TO_DESCRIPTION + metadataDTO.getRecipient().getName())
              .build()
      );
    }

    createReplacesEvent(metadataDTO)
        .ifPresent(events::add);

    createReplacedEvent(metadataDTO)
        .ifPresent(events::add);

    return events.stream()
        .sorted(Comparator.comparing(CertificateEvent::getTimestamp).reversed())
        .toList();
  }

  private static boolean isSent(CertificateMetadataDTO metadataDTO) {
    if (metadataDTO.getRecipient() == null) {
      return false;
    }
    return metadataDTO.getRecipient().getSent() != null;
  }

  private Optional<CertificateEvent> createReplacedEvent(CertificateMetadataDTO metadataDTO) {
    if (noChildRelations(metadataDTO)) {
      return Optional.empty();
    }

    return Arrays.stream(metadataDTO.getRelations().getChildren())
        .filter(isReplacedCertificate())
        .filter(isSigned())
        .max(Comparator.comparing(CertificateRelation::getCreated))
        .map(relation -> createEvent(relation, EVENT_REPLACED));
  }

  private static boolean noChildRelations(CertificateMetadataDTO metadataDTO) {
    return metadataDTO.getRelations() == null
        || metadataDTO.getRelations().getChildren() == null;
  }

  private Predicate<? super CertificateRelation> isSigned() {
    return child -> CertificateStatus.SIGNED.equals(child.getStatus());
  }

  private static Predicate<CertificateRelation> isReplacedCertificate() {
    return child ->
        child.getType() == CertificateRelationType.REPLACED
            || child.getType() == CertificateRelationType.COMPLEMENTED;
  }

  private Optional<CertificateEvent> createReplacesEvent(CertificateMetadataDTO metadataDTO) {
    if (!isReplacingCertificate(metadataDTO)) {
      return Optional.empty();
    }

    return Optional.of(
        createEvent(metadataDTO.getRelations().getParent(), EVENT_REPLACES)
    );
  }

  private boolean isReplacingCertificate(CertificateMetadataDTO metadataDTO) {
    if (metadataDTO.getRelations() == null || metadataDTO.getRelations().getParent() == null) {
      return false;
    }

    final var relationType = metadataDTO.getRelations().getParent().getType();
    return CertificateRelationType.REPLACED.equals(relationType)
        || CertificateRelationType.COMPLEMENTED.equals(relationType);
  }

  private static CertificateEvent createEvent(CertificateRelation relation, String description) {
    return CertificateEvent.builder()
        .certificateId(relation.getCertificateId())
        .timestamp(relation.getCreated())
        .description(description)
        .build();
  }
}
