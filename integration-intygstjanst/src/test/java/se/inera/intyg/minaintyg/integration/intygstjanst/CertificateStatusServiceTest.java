package se.inera.intyg.minaintyg.integration.intygstjanst;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.minaintyg.integration.api.certificate.CertificateConstants.DAYS_LIMIT_FOR_STATUS_NEW;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

@ExtendWith(MockitoExtension.class)
class CertificateStatusServiceTest {

  private static final LocalDateTime NEW_ISSUED = LocalDateTime.now()
      .minusDays(DAYS_LIMIT_FOR_STATUS_NEW).plusDays(1);
  private static final LocalDateTime OLD_ISSUED = LocalDateTime.now()
      .minusDays(DAYS_LIMIT_FOR_STATUS_NEW).minusDays(1);
  private static final CertificateRecipientDTO SENT_RECIPIENT = CertificateRecipientDTO
      .builder()
      .id("id")
      .sent(LocalDateTime.now())
      .build();
  private static final CertificateRecipientDTO NOT_SENT_RECIPIENT = CertificateRecipientDTO
      .builder()
      .id("id")
      .build();
  private static final List<CertificateRelationDTO> REPLACED_RELATIONS = List.of(
      CertificateRelationDTO
          .builder()
          .type(CertificateRelationType.REPLACED)
          .build());
  private static final List<CertificateRelationDTO> REPLACES_RELATIONS = List.of(
      CertificateRelationDTO
          .builder()
          .type(CertificateRelationType.REPLACES)
          .build());

  @InjectMocks
  CertificateStatusService certificateStatusService;

  @Test
  void shouldIncludeNewIfNewerThanTheLimitForNewCertificates() {
    final var response = certificateStatusService.get(Collections.emptyList(), null, NEW_ISSUED);

    assertEquals(1, response.size());
    assertEquals(CertificateStatusType.NEW, response.get(0));
  }

  @Test
  void shouldNotIncludeNewIfOlderThanTheLimitForNewCertificates() {
    final var response = certificateStatusService.get(Collections.emptyList(), null, OLD_ISSUED);

    assertEquals(0, response.size());
  }

  @Test
  void shouldNotIncludeSentOrNotSentIfRecipientIsNull() {
    final var response = certificateStatusService.get(Collections.emptyList(), null, null);

    assertEquals(0, response.size());
  }

  @Test
  void shouldIncludeNotSentIfRecipientExistsButSentIsNull() {
    final var response = certificateStatusService.get(Collections.emptyList(), NOT_SENT_RECIPIENT,
        null);

    assertEquals(1, response.size());
    assertEquals(CertificateStatusType.NOT_SENT, response.get(0));
  }

  @Test
  void shouldIncludeSentIfRecipientAndSentExists() {
    final var response = certificateStatusService.get(Collections.emptyList(), SENT_RECIPIENT,
        null);

    assertEquals(1, response.size());
    assertEquals(CertificateStatusType.SENT, response.get(0));
  }

  @Test
  void shouldIncludeReplacedIfRelationExists() {
    final var response = certificateStatusService.get(REPLACED_RELATIONS, null, null);

    assertEquals(1, response.size());
    assertEquals(CertificateStatusType.REPLACED, response.get(0));
  }

  @Test
  void shouldNotIncludeStatusIfRelationIsReplaces() {
    final var response = certificateStatusService.get(REPLACES_RELATIONS, null, null);

    assertEquals(0, response.size());
  }

  @Test
  void shouldNotIncludeStatusIfRelationsIsEmpty() {
    final var response = certificateStatusService.get(Collections.emptyList(), null, null);

    assertEquals(0, response.size());
  }

  @Test
  void shouldOnlyIncludeReplacedIfCertificateIsBothSentAndReplaced() {
    final var response = certificateStatusService.get(REPLACED_RELATIONS, SENT_RECIPIENT, null);

    assertEquals(1, response.size());
    assertEquals(CertificateStatusType.REPLACED, response.get(0));
  }

  @Test
  void shouldOnlyIncludeReplacedIfCertificateIsBothNotSentAndReplaced() {
    final var response = certificateStatusService.get(REPLACED_RELATIONS, NOT_SENT_RECIPIENT, null);

    assertEquals(1, response.size());
    assertEquals(CertificateStatusType.REPLACED, response.get(0));
  }

  @Test
  void shouldIncludeBothSentAndNewIfSentAndNewerThanTheLimitForNewCertificates() {
    final var response = certificateStatusService.get(Collections.emptyList(), SENT_RECIPIENT,
        NEW_ISSUED);

    assertEquals(2, response.size());
    assertEquals(CertificateStatusType.SENT, response.get(0));
    assertEquals(CertificateStatusType.NEW, response.get(1));
  }

  @Test
  void shouldIncludeBothNotSentAndNewIfSentIsNullAndNewerThanTheLimitForNewCertificates() {
    final var response = certificateStatusService.get(Collections.emptyList(), NOT_SENT_RECIPIENT,
        NEW_ISSUED);

    assertEquals(2, response.size());
    assertEquals(CertificateStatusType.NOT_SENT, response.get(0));
    assertEquals(CertificateStatusType.NEW, response.get(1));
  }
}