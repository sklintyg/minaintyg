package se.inera.intyg.minaintyg.integration.webcert.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateEvent;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelation;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelations;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateStatus;

class EventConverterTest {

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
        CertificateEvent.builder()
            .certificateId("id")
            .description("Ersatt av vården med ett nytt intyg")
            .timestamp(LocalDateTime.now())
            .build()
    );

    final var metadataDTO = CertificateMetadataDTO.builder()
        .relations(
            CertificateRelations.builder()
                .children(
                    new CertificateRelation[]{
                        CertificateRelation.builder()
                            .type(CertificateRelationType.REPLACED)
                            .certificateId(expectedEvent.get(0).getCertificateId())
                            .created(expectedEvent.get(0).getTimestamp())
                            .status(CertificateStatus.SIGNED)
                            .build()
                    }
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
        CertificateEvent.builder()
            .certificateId("id")
            .description("Ersatt av vården med ett nytt intyg")
            .timestamp(LocalDateTime.now())
            .build()
    );

    final var metadataDTO = CertificateMetadataDTO.builder()
        .relations(
            CertificateRelations.builder()
                .children(
                    new CertificateRelation[]{
                        CertificateRelation.builder()
                            .type(CertificateRelationType.COMPLEMENTED)
                            .certificateId(expectedEvent.get(0).getCertificateId())
                            .created(expectedEvent.get(0).getTimestamp())
                            .status(CertificateStatus.SIGNED)
                            .build()
                    }
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
        CertificateEvent.builder()
            .certificateId("id")
            .description("Ersätter ett intyg som inte längre är aktuellt")
            .timestamp(LocalDateTime.now())
            .build()
    );

    final var metadataDTO = CertificateMetadataDTO.builder()
        .relations(
            CertificateRelations.builder()
                .parent(CertificateRelation.builder()
                    .type(CertificateRelationType.REPLACED)
                    .certificateId(expectedEvent.get(0).getCertificateId())
                    .created(expectedEvent.get(0).getTimestamp())
                    .status(CertificateStatus.SIGNED)
                    .build())
                .build()
        )
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(expectedEvent, actualEvents);
  }

  @Test
  void shallConvertComplementedCertificateToReplacesEvent() {
    final var expectedEvent = List.of(
        CertificateEvent.builder()
            .certificateId("id")
            .description("Ersätter ett intyg som inte längre är aktuellt")
            .timestamp(LocalDateTime.now())
            .build()
    );

    final var metadataDTO = CertificateMetadataDTO.builder()
        .relations(
            CertificateRelations.builder()
                .parent(CertificateRelation.builder()
                    .type(CertificateRelationType.COMPLEMENTED)
                    .certificateId(expectedEvent.get(0).getCertificateId())
                    .created(expectedEvent.get(0).getTimestamp())
                    .status(CertificateStatus.SIGNED)
                    .build())
                .build()
        )
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(expectedEvent, actualEvents);
  }

  @Test
  void shallConvertReplacingCertificateThatHasBeenReplacedToReplacedEvent() {
    final var expectedEvent = List.of(
        CertificateEvent.builder()
            .certificateId("id")
            .description("Ersätter ett intyg som inte längre är aktuellt")
            .timestamp(LocalDateTime.now())
            .build(),
        CertificateEvent.builder()
            .certificateId("id")
            .description("Ersatt av vården med ett nytt intyg")
            .timestamp(LocalDateTime.now())
            .build());

    final var metadataDTO = CertificateMetadataDTO.builder()
        .relations(
            CertificateRelations.builder()
                .parent(CertificateRelation.builder()
                    .type(CertificateRelationType.REPLACED)
                    .certificateId(expectedEvent.get(0).getCertificateId())
                    .created(expectedEvent.get(0).getTimestamp())
                    .status(CertificateStatus.SIGNED)
                    .build())
                .children(
                    new CertificateRelation[]{
                        CertificateRelation.builder()
                            .type(CertificateRelationType.REPLACED)
                            .certificateId(expectedEvent.get(1).getCertificateId())
                            .created(expectedEvent.get(1).getTimestamp())
                            .status(CertificateStatus.SIGNED)
                            .build()
                    }
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
                    new CertificateRelation[]{
                        CertificateRelation.builder()
                            .type(CertificateRelationType.REPLACED)
                            .certificateId("id")
                            .created(LocalDateTime.now())
                            .status(CertificateStatus.REVOKED)
                            .build()
                    }
                )
                .build()
        )
        .build();

    final var actualEvents = eventConverter.convert(metadataDTO);
    assertEquals(Collections.emptyList(), actualEvents);
  }
}