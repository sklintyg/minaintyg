package se.inera.intyg.minaintyg.integration.intygstjanst;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;

@ExtendWith(MockitoExtension.class)
class CertificateStatusServiceImplTest {

  private static final LocalDateTime NEW_ISSUED = LocalDateTime.now();
  private static final LocalDateTime OLD_ISSUED = LocalDateTime.now().minusMonths(1);
  private static final CertificateRecipientDTO SENT_RECIPIENT = CertificateRecipientDTO
      .builder()
      .id("id")
      .sent(LocalDateTime.now())
      .build();
  private static final CertificateRecipientDTO NOT_SENT_RECIPIENT = CertificateRecipientDTO
      .builder()
      .id("id")
      .build();
  private static final List<CertificateRelationDTO> RENEWED_RELATIONS = List.of(
      CertificateRelationDTO
          .builder()
          .type(CertificateRelationType.RENEWED)
          .build());
  private static final List<CertificateRelationDTO> RENEWS_RELATIONS = List.of(
      CertificateRelationDTO
          .builder()
          .type(CertificateRelationType.RENEWS)
          .build());

  @InjectMocks
  CertificateStatusServiceImpl certificateStatusService;

  @Test
  void shouldIncludeNewIfNewerThanAMonth() {
    final var response = certificateStatusService.get(Collections.emptyList(), null, NEW_ISSUED);

    assertEquals(1, response.size());
    assertEquals(CertificateStatusType.NEW, response.get(0));
  }

  @Test
  void shouldNotIncludeNewIfOlderThanAMonth() {
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
  void shouldIncludeRenewedIfRelationExists() {
    final var response = certificateStatusService.get(RENEWED_RELATIONS, null, null);

    assertEquals(1, response.size());
    assertEquals(CertificateStatusType.RENEWED, response.get(0));
  }

  @Test
  void shouldNotIncludeStatusIfRelationIsRenews() {
    final var response = certificateStatusService.get(RENEWS_RELATIONS, null, null);

    assertEquals(0, response.size());
  }

  @Test
  void shouldNotIncludeStatusIfRelationsIsEmpty() {
    final var response = certificateStatusService.get(Collections.emptyList(), null, null);

    assertEquals(0, response.size());
  }

  @Test
  void shouldNotIncludeStatusIfRelationsIsNull() {
    final var response = certificateStatusService.get(null, null, null);

    assertEquals(0, response.size());
  }

  @Test
  void shouldNotIncludeSeveralStatuses() {
    final var response = certificateStatusService.get(RENEWED_RELATIONS, SENT_RECIPIENT,
        NEW_ISSUED);

    assertEquals(3, response.size());
  }
}