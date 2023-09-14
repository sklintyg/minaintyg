package se.inera.intyg.minaintyg.certificate.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateRelation;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateRelationType;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class CertificateEventServiceImplTest {
    private static final CertificateRecipient SENT_RECIPIENT = CertificateRecipient
            .builder()
            .id("id")
            .name("Namn")
            .sent(LocalDateTime.now())
            .build();
    private static final CertificateRecipient NOT_SENT_RECIPIENT = CertificateRecipient
            .builder()
            .id("id")
            .build();
    private static final List<CertificateRelation> RENEWED_RELATIONS = List.of(CertificateRelation
            .builder()
            .type(CertificateRelationType.RENEWED)
            .certificateId("certificateId1")
            .timestamp(LocalDateTime.now().toString())
            .build());
    private static final List<CertificateRelation> RENEWS_RELATIONS = List.of(CertificateRelation
            .builder()
            .type(CertificateRelationType.RENEWS)
            .certificateId("certificateId2")
            .timestamp(LocalDateTime.now().toString())
            .build());

    @InjectMocks
    CertificateEventServiceImpl certificateEventService;

    @Nested
    class SentEvent {
        @Test
        void shouldNotIncludeEventIfRecipientIsNull() {
            final var response = certificateEventService.get(Collections.emptyList(), null);

            assertEquals(0, response.size());
        }

        @Test
        void shouldIncludeEventIfNotSent() {
            final var response = certificateEventService.get(Collections.emptyList(), NOT_SENT_RECIPIENT);

            assertEquals(0, response.size());
        }

        @Test
        void shouldIncludeEventIfSent() {
            final var response = certificateEventService.get(Collections.emptyList(), SENT_RECIPIENT);

            assertEquals(1, response.size());
        }

        @Test
        void shouldIncludeDescription() {
            final var response = certificateEventService.get(Collections.emptyList(), SENT_RECIPIENT);

            assertEquals("Skickat till Namn", response.get(0).getDescription());
        }

        @Test
        void shouldIncludeTimestamp() {
            final var response = certificateEventService.get(Collections.emptyList(), SENT_RECIPIENT);

            assertEquals(SENT_RECIPIENT.getSent().toString(), response.get(0).getTimestamp());
        }

        @Test
        void shouldNotIncludeCertificateId() {
            final var response = certificateEventService.get(Collections.emptyList(), SENT_RECIPIENT);

            assertNull(response.get(0).getCertificateId());
        }
    }

    @Nested
    class RenewedEvent {

        @Test
        void shouldIncludeEventIfRelationExists() {
            final var response = certificateEventService.get(RENEWED_RELATIONS, null);

            assertEquals(1, response.size());
        }

        @Test
        void shouldIncludeDescription() {
            final var response = certificateEventService.get(RENEWED_RELATIONS, null);

            assertEquals("Ersattes av vården med ett nytt intyg", response.get(0).getDescription());
        }

        @Test
        void shouldIncludeCertificateId() {
            final var response = certificateEventService.get(RENEWED_RELATIONS, null);

            assertEquals(RENEWED_RELATIONS.get(0).getCertificateId(), response.get(0).getCertificateId());
        }

        @Test
        void shouldIncludeTimestamp() {
            final var response = certificateEventService.get(RENEWED_RELATIONS, null);

            assertEquals(RENEWED_RELATIONS.get(0).getTimestamp(), response.get(0).getTimestamp());
        }
    }

    @Nested
    class RenewsEvent {

        @Test
        void shouldIncludeEventIfRelationExists() {
            final var response = certificateEventService.get(RENEWS_RELATIONS, null);

            assertEquals(1, response.size());
        }

        @Test
        void shouldIncludeDescription() {
            final var response = certificateEventService.get(RENEWS_RELATIONS, null);

            assertEquals("Ersätter ett intyg som inte längre är aktuellt", response.get(0).getDescription());
        }

        @Test
        void shouldIncludeCertificateId() {
            final var response = certificateEventService.get(RENEWS_RELATIONS, null);

            assertEquals(RENEWS_RELATIONS.get(0).getCertificateId(), response.get(0).getCertificateId());
        }

        @Test
        void shouldIncludeTimestamp() {
            final var response = certificateEventService.get(RENEWS_RELATIONS, null);

            assertEquals(RENEWS_RELATIONS.get(0).getTimestamp(), response.get(0).getTimestamp());
        }
    }

    @Test
    void shouldNotIncludeSeveralStatuses() {
        final var response = certificateEventService.get(RENEWED_RELATIONS, SENT_RECIPIENT);

        assertEquals(2, response.size());
    }
}