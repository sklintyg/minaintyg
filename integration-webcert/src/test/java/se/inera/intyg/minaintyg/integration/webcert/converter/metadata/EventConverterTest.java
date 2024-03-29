package se.inera.intyg.minaintyg.integration.webcert.converter.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateEvent;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelation;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelations;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateStatus;

class EventConverterTest {

  public static final String REPLACED_DESCRIPTION = "Ersatt av vården med ett nytt intyg";
  public static final String REPLACES_DESCRIPTION = "Ersätter ett intyg som inte längre är aktuellt";
  public static final LocalDateTime TIMESTAMP = LocalDateTime.now();
  private final EventConverter eventConverter = new EventConverter();

  @Test
  void shallConvertSentCertificateToSentEvent() {
    final var expectedEvent = List.of(
        CertificateEvent.builder()
            .description("Skickat till recipientName")
            .timestamp(TIMESTAMP)
            .build()
    );

    final var metadataDTO = CertificateMetadataDTO.builder()
        .recipient(CertificateRecipient.builder()
            .name("recipientName")
            .sent(TIMESTAMP)
            .build())
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(expectedEvent, actualEvents);
  }

  @Test
  void shallReturnEmptyListIfUnsentCertificate() {
    final var metadataDTO = CertificateMetadataDTO.builder()
        .recipient(CertificateRecipient.builder()
            .id("id")
            .name("recipientName")
            .build())
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(Collections.emptyList(), actualEvents);
  }

  @Test
  void shallReturnEmptyListIfNoRecipient() {
    final var metadataDTO = CertificateMetadataDTO.builder().build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(Collections.emptyList(), actualEvents);
  }

  @Test
  void shallConvertReplacedCertificateToReplacedEventIfReplacingCertificateIsSigned() {
    final var expectedEvent = List.of(
        createReplaceEvent(REPLACED_DESCRIPTION)
    );

    final var metadataDTO = CertificateMetadataDTO.builder()
        .relations(
            CertificateRelations.builder()
                .children(
                    createChildRelations(
                        createRelation(expectedEvent.get(0), CertificateRelationType.REPLACED,
                            CertificateStatus.SIGNED)
                    )
                )
                .build()
        )
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(expectedEvent, actualEvents);
  }

  @Test
  void shallNotConvertReplacedCertificateToReplacedEventIfReplacingCertificateUnsigned() {
    final var metadataDTO = CertificateMetadataDTO.builder()
        .relations(
            CertificateRelations.builder()
                .children(
                    createChildRelations(
                        createRelation(createReplaceEvent(REPLACED_DESCRIPTION),
                            CertificateRelationType.REPLACED, CertificateStatus.UNSIGNED)
                    )
                )
                .build()
        )
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(Collections.EMPTY_LIST, actualEvents);
  }
  @Test
  void shallNotConvertReplacedCertificateToReplacedEventIfReplacingCertificateRevoked() {
    final var metadataDTO = CertificateMetadataDTO.builder()
        .relations(
            CertificateRelations.builder()
                .children(
                    createChildRelations(
                        createRelation(createReplaceEvent(REPLACED_DESCRIPTION),
                            CertificateRelationType.REPLACED, CertificateStatus.REVOKED)
                    )
                )
                .build()
        )
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(Collections.EMPTY_LIST, actualEvents);
  }

  @Test
  void shallNotConvertReplacedCertificateToReplacedEventIfReplacingCertificateLocked() {
    final var metadataDTO = CertificateMetadataDTO.builder()
        .relations(
            CertificateRelations.builder()
                .children(
                    createChildRelations(
                        createRelation(createReplaceEvent(REPLACED_DESCRIPTION),
                            CertificateRelationType.REPLACED, CertificateStatus.LOCKED)
                    )
                )
                .build()
        )
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(Collections.EMPTY_LIST, actualEvents);
  }

  @Test
  void shallNotConvertReplacedCertificateToReplacedEventIfReplacingCertificateLockedRevoked() {
    final var metadataDTO = CertificateMetadataDTO.builder()
        .relations(
            CertificateRelations.builder()
                .children(
                    createChildRelations(
                        createRelation(createReplaceEvent(REPLACED_DESCRIPTION),
                            CertificateRelationType.REPLACED, CertificateStatus.LOCKED_REVOKED)
                    )
                )
                .build()
        )
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(Collections.EMPTY_LIST, actualEvents);
  }

  @Test
  void shallConvertComplementedCertificateToReplacedEvent() {
    final var expectedEvent = List.of(
        createReplaceEvent(REPLACED_DESCRIPTION)
    );

    final var metadataDTO = CertificateMetadataDTO.builder()
        .relations(
            CertificateRelations.builder()
                .children(
                    createChildRelations(
                        createRelation(expectedEvent.get(0), CertificateRelationType.COMPLEMENTED)
                    )
                )
                .build()
        )
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(expectedEvent, actualEvents);
  }

  @Test
  void shallConvertLatestReplacedAndComplementedCertificateToReplacedEvent() {
    final var expectedEvent = List.of(
        createReplaceEvent(REPLACED_DESCRIPTION)
    );

    final var metadataDTO = CertificateMetadataDTO.builder()
        .relations(
            CertificateRelations.builder()
                .children(
                    createChildRelations(
                        CertificateRelation.builder()
                            .type(CertificateRelationType.COMPLEMENTED)
                            .certificateId("differentId")
                            .created(TIMESTAMP.minusDays(1))
                            .status(CertificateStatus.SIGNED)
                            .build(),
                        createRelation(expectedEvent.get(0), CertificateRelationType.REPLACED)
                    )
                )
                .build()
        )
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(expectedEvent, actualEvents);
  }

  @Test
  void shallConvertReplacingCertificateToReplacesEvent() {
    final var expectedEvent = List.of(
        createReplaceEvent(REPLACES_DESCRIPTION)
    );

    final var metadataDTO = CertificateMetadataDTO.builder()
        .relations(
            CertificateRelations.builder()
                .parent(
                    createRelation(expectedEvent.get(0), CertificateRelationType.REPLACED)
                )
                .build()
        )
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(expectedEvent, actualEvents);
  }

  @Test
  void shallConvertComplementedCertificateToReplacesEvent() {
    final var expectedEvent = List.of(
        createReplaceEvent(REPLACES_DESCRIPTION)
    );

    final var metadataDTO = CertificateMetadataDTO.builder()
        .relations(
            CertificateRelations.builder()
                .parent(
                    createRelation(expectedEvent.get(0), CertificateRelationType.COMPLEMENTED)
                )
                .build()
        )
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(expectedEvent, actualEvents);
  }

  @Test
  void shallConvertReplacingCertificateThatHasBeenReplacedToReplacedEvent() {
    final var expectedEvent = List.of(
        createEvent(REPLACES_DESCRIPTION, LocalDateTime.now().plusDays(1)),
        createEvent(REPLACED_DESCRIPTION, LocalDateTime.now())
    );

    final var metadataDTO = CertificateMetadataDTO.builder()
        .relations(
            CertificateRelations.builder()
                .parent(
                    createRelation(expectedEvent.get(0), CertificateRelationType.REPLACED)
                )
                .children(
                    createChildRelations(
                        createRelation(expectedEvent.get(1), CertificateRelationType.REPLACED)
                    )
                )
                .build()
        )
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(expectedEvent, actualEvents);
  }

  @Test
  void shallReturnEmptyListIfReplacingCertificateIsRevoked() {
    final var metadataDTO = CertificateMetadataDTO.builder()
        .relations(
            CertificateRelations.builder()
                .children(
                    createChildRelations(CertificateRelation.builder()
                        .type(CertificateRelationType.REPLACED)
                        .certificateId("id")
                        .created(LocalDateTime.now())
                        .status(CertificateStatus.REVOKED)
                        .build())
                )
                .build()
        )
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(Collections.emptyList(), actualEvents);
  }

  @Test
  void shallSortEventsOnCreationTimestamp() {
    final var sent = LocalDateTime.now();
    final var replaced = LocalDateTime.now().plusDays(1);

    final var expectedEvents = List.of(
        createEvent(REPLACES_DESCRIPTION, replaced, "id"),
        createEvent("Skickat till recipientName", sent)
    );

    final var metadataDTO = CertificateMetadataDTO.builder()
        .recipient(CertificateRecipient.builder()
            .name("recipientName")
            .sent(sent)
            .build())
        .relations(
            CertificateRelations.builder()
                .parent(
                    createRelation(expectedEvents.get(0), CertificateRelationType.COMPLEMENTED)
                )
                .build()
        )
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(expectedEvents, actualEvents);
  }

  private static CertificateEvent createReplaceEvent(String description) {
    return createEvent(description, TIMESTAMP, "id");
  }

  private static CertificateEvent createEvent(String description, LocalDateTime timestamp) {
    return createEvent(description, timestamp, null);
  }

  private static CertificateEvent createEvent(
      String description,
      LocalDateTime timestamp,
      String certificateId) {
    return CertificateEvent.builder()
        .certificateId(certificateId)
        .description(description)
        .timestamp(timestamp)
        .build();
  }

  @NotNull
  private static CertificateRelation[] createChildRelations(CertificateRelation... relations) {
    return relations;
  }

  private static CertificateRelation createRelation(CertificateEvent expectedEvent,
      CertificateRelationType certificateRelationType) {
    return createRelation(expectedEvent, certificateRelationType, CertificateStatus.SIGNED);
  }

  @NotNull
  private static CertificateRelation createRelation(CertificateEvent expectedEvent,
      CertificateRelationType certificateRelationType, CertificateStatus certificateStatus) {
    return CertificateRelation.builder()
        .type(certificateRelationType)
        .certificateId(expectedEvent.getCertificateId())
        .created(expectedEvent.getTimestamp())
        .status(certificateStatus)
        .build();
  }
}