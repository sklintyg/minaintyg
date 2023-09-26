package se.inera.intyg.minaintyg.integration.intygstjanst;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

class CertificateStatusFactoryTest {

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
    void shouldReturnReplacedIfCorrectRelationType() {
      final var result = CertificateStatusFactory.replaced(replaced);

      assertEquals(CertificateStatusType.REPLACED, result.get());
    }

    @Test
    void shouldReturnEmptyIfRelationIsNotReplaced() {
      final var result = CertificateStatusFactory.replaced(replaces);

      assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyIfRelationIsNull() {
      final var result = CertificateStatusFactory.replaced(null);

      assertTrue(result.isEmpty());
    }
  }

  @Nested
  class SentEvent {

    @Test
    void shouldReturnSentIfRecipientHasSentTimestamp() {
      final var result = CertificateStatusFactory.sent(recipient);

      assertEquals(CertificateStatusType.SENT, result.get());
    }

    @Test
    void shouldReturnEmptyIfRecipientIsNull() {
      final var result = CertificateStatusFactory.sent(null);

      assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnNotSentIfNoSentValue() {
      final var result = CertificateStatusFactory.sent(CertificateRecipientDTO.builder().build());

      assertEquals(CertificateStatusType.NOT_SENT, result.get());
    }
  }

  @Nested
  class NewEvent {

    @Test
    void shouldReturnNewIfIssuedIsLessThanOneMonthBack() {
      final var result = CertificateStatusFactory.newStatus(LocalDateTime.now());

      assertEquals(CertificateStatusType.NEW, result.get());
    }

    @Test
    void shouldReturnEmptyIfIssuedIsMoreThanOneMonthBack() {
      final var result = CertificateStatusFactory.newStatus(LocalDateTime.now().minusMonths(2));

      assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnNewIfIssuedIsOneMonthBack() {
      final var result = CertificateStatusFactory.newStatus(LocalDateTime.now().minusMonths(1));

      assertEquals(CertificateStatusType.NEW, result.get());
    }
  }
}