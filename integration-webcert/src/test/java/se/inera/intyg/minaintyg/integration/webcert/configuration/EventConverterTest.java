package se.inera.intyg.minaintyg.integration.webcert.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateEvent;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelation;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelations;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateStatus;

class EventConverterTest {

  public static final String REPLACED_DESCRIPTION = "Ersatt av vården med ett nytt intyg";
  public static final String REPLACES_DESCRIPTION = "Ersätter ett intyg som inte längre är aktuellt";
  private final EventConverter eventConverter = new EventConverter();

  @Test
  void shallConvertSentCertificateToSentEvent() {
    final var expectedEvent = List.of(
        CertificateEvent.builder()
            .description("Skickat till recipientName")
            .build()
    );

    final var metadataDTO = CertificateMetadataDTO.builder()
        .sent(true)
        .sentTo("recipientName")
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(expectedEvent, actualEvents);
  }

  @Test
  void shallReturnEmptyListIfUnsentCertificate() {
    final var metadataDTO = CertificateMetadataDTO.builder()
        .sent(false)
        .sentTo("recipientName")
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(Collections.emptyList(), actualEvents);
  }

  @Test
  void shallConvertReplacedCertificateToReplacedEvent() {
    final var expectedEvent = List.of(
        createReplaceEvent(REPLACED_DESCRIPTION)
    );

    final var metadataDTO = CertificateMetadataDTO.builder()
        .relations(
            CertificateRelations.builder()
                .children(
                    createChildRelations(
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
                            .created(LocalDateTime.now().minusDays(1))
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

  @NotNull
  private static CertificateRelation[] createChildRelations(CertificateRelation... relations) {
    return relations;
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
        createReplaceEvent(REPLACES_DESCRIPTION),
        createReplaceEvent(REPLACED_DESCRIPTION)
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

  private static CertificateEvent createReplaceEvent(String description) {
    return CertificateEvent.builder()
        .certificateId("id")
        .description(description)
        .timestamp(LocalDateTime.now())
        .build();
  }

  @NotNull
  private static CertificateRelation createRelation(CertificateEvent expectedEvent,
      CertificateRelationType certificateRelationType) {
    return CertificateRelation.builder()
        .type(certificateRelationType)
        .certificateId(expectedEvent.getCertificateId())
        .created(expectedEvent.getTimestamp())
        .status(CertificateStatus.SIGNED)
        .build();
  }
}