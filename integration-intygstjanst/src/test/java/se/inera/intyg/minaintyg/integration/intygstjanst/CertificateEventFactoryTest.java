package se.inera.intyg.minaintyg.integration.intygstjanst;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

class CertificateEventFactoryTest {

  private static final CertificateRelationDTO replaced = CertificateRelationDTO
      .builder()
      .certificateId("CERTIFICATE_ID")
      .timestamp(LocalDateTime.now())
      .type(CertificateRelationType.REPLACED)
      .build();

  private static final CertificateRelationDTO replaces = CertificateRelationDTO
      .builder()
      .certificateId("CERTIFICATE_ID")
      .timestamp(LocalDateTime.now())
      .type(CertificateRelationType.REPLACES)
      .build();

  private static final CertificateRecipientDTO recipient = CertificateRecipientDTO
      .builder()
      .name("Name")
      .id("id")
      .sent(LocalDateTime.now())
      .build();

  @Nested
  class ReplacedEvent {

    @Test
    void shouldReturnCertificateId() {
      final var result = CertificateEventFactory.replaced(replaced);

      assertEquals(replaced.getCertificateId(), result.get().getCertificateId());
    }

    @Test
    void shouldReturnTimestamp() {
      final var result = CertificateEventFactory.replaced(replaced);

      assertEquals(replaced.getTimestamp(), result.get().getTimestamp());
    }

    @Test
    void shouldReturnDescription() {
      final var result = CertificateEventFactory.replaced(replaced);

      assertEquals("Ersattes av vården med ett nytt intyg", result.get().getDescription());
    }

    @Test
    void shouldReturnEmptyIfRelationIsNull() {
      final var result = CertificateEventFactory.replaced(null);

      assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyIfRelationHasWrongType() {
      final var result = CertificateEventFactory.replaced(replaces);

      assertTrue(result.isEmpty());
    }
  }

  @Nested
  class ReplacesEvent {

    @Test
    void shouldReturnCertificateId() {
      final var result = CertificateEventFactory.replaces(replaces);

      assertEquals(replaces.getCertificateId(), result.get().getCertificateId());
    }

    @Test
    void shouldReturnTimestamp() {
      final var result = CertificateEventFactory.replaces(replaces);

      assertEquals(replaces.getTimestamp(), result.get().getTimestamp());
    }

    @Test
    void shouldReturnDescription() {
      final var result = CertificateEventFactory.replaces(replaces);

      assertEquals("Ersätter ett intyg som inte längre är aktuellt", result.get().getDescription());
    }

    @Test
    void shouldReturnEmptyIfRelationIsNull() {
      final var result = CertificateEventFactory.replaces(null);

      assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyIfRelationHasWrongType() {
      final var result = CertificateEventFactory.replaces(replaced);

      assertTrue(result.isEmpty());
    }
  }

  @Nested
  class SentEvent {

    @Test
    void shouldNotSetCertificateId() {
      final var result = CertificateEventFactory.sent(recipient);

      assertNull(result.get().getCertificateId());
    }

    @Test
    void shouldReturnTimestamp() {
      final var result = CertificateEventFactory.sent(recipient);

      assertEquals(recipient.getSent(), result.get().getTimestamp());
    }

    @Test
    void shouldReturnDescription() {
      final var result = CertificateEventFactory.sent(recipient);

      assertEquals("Skickat till Name", result.get().getDescription());
    }

    @Test
    void shouldReturnEmptyIfRecipientIsNull() {
      final var result = CertificateEventFactory.sent(null);

      assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyIfNotSent() {
      final var result = CertificateEventFactory.sent(CertificateRecipientDTO.builder().build());

      assertTrue(result.isEmpty());
    }
  }
}