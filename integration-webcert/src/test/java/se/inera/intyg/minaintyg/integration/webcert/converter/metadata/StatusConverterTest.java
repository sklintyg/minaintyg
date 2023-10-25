package se.inera.intyg.minaintyg.integration.webcert.converter.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelation;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelationType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRelations;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateStatus;

class StatusConverterTest {

  public static final LocalDateTime OLD_DATE = LocalDateTime.now().minusDays(15);
  public static final LocalDateTime NEW_DATE = LocalDateTime.now().minusDays(13);
  public static final LocalDateTime LAST_DAY_AS_NEW = LocalDateTime.now().minusDays(14);
  public static final String RECIPIENT_ID = "Id";
  private final StatusConverter statusConverter = new StatusConverter();

  @Nested
  class Sent {

    @Test
    void shallReturnEmptyListIfCertificateHasNoRecipient() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(OLD_DATE)
          .recipient(
              createRecipient(null, LocalDateTime.now())
          )
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(Collections.emptyList(), actualStatuses);
    }

    @Test
    void shallConvertToSentIfCertificateIsSentToRecipient() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(OLD_DATE)
          .recipient(
              createRecipient(RECIPIENT_ID, LocalDateTime.now())
          )
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(List.of(CertificateStatusType.SENT), actualStatuses);
    }
  }

  @Nested
  class NotSent {

    @Test
    void shallReturnEmptyListIfCertificateHasNoRecipient() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(OLD_DATE)
          .recipient(
              createRecipient(null, null)
          )
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(Collections.emptyList(), actualStatuses);
    }

    @Test
    void shallConvertToNotSentIfCertificateCanBeSentToRecipientButHasNot() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(OLD_DATE)
          .recipient(
              createRecipient(RECIPIENT_ID, null)
          )
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(List.of(CertificateStatusType.NOT_SENT), actualStatuses);
    }
  }

  @Nested
  class New {

    @Test
    void shallConvertToNewIfCertificateWasCreatedBeforeDateLimitForNewStatus() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(NEW_DATE)
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(List.of(CertificateStatusType.NEW), actualStatuses);
    }

    @Test
    void shallConvertToNewIfCertificateWasCreatedOnTheLastDayForNewStatus() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(LAST_DAY_AS_NEW)
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(List.of(CertificateStatusType.NEW), actualStatuses);
    }

    @Test
    void shallReturnEmptyListIfCertificateWasCreatedAfterDateLimitForNewStatus() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(OLD_DATE)
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(Collections.emptyList(), actualStatuses);
    }
  }

  @Nested
  class Replaced {

    @Test
    void shallConvertToReplacedIfCertificateIsReplacedAndReplacingCertificateIsSigned() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(OLD_DATE)
          .relations(
              createChild(CertificateRelationType.REPLACED, CertificateStatus.SIGNED)
          )
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(List.of(CertificateStatusType.REPLACED), actualStatuses);
    }

    @Test
    void shallNotConvertToReplacedIfCertificateIsReplacedAndReplacingCertificateIsUnsigned() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(OLD_DATE)
          .relations(
              createChild(CertificateRelationType.REPLACED, CertificateStatus.UNSIGNED)
          )
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(Collections.EMPTY_LIST, actualStatuses);
    }

    @Test
    void shallNotConvertToReplacedIfCertificateIsReplacedAndReplacingCertificateIsRevoked() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(OLD_DATE)
          .relations(
              createChild(CertificateRelationType.REPLACED, CertificateStatus.REVOKED)
          )
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(Collections.EMPTY_LIST, actualStatuses);
    }

    @Test
    void shallNotConvertToReplacedIfCertificateIsReplacedAndReplacingCertificateIsLocked() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(OLD_DATE)
          .relations(
              createChild(CertificateRelationType.REPLACED, CertificateStatus.LOCKED)
          )
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(Collections.EMPTY_LIST, actualStatuses);
    }

    @Test
    void shallNotConvertToReplacedIfCertificateIsReplacedAndReplacingCertificateIsLockedRevoked() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(OLD_DATE)
          .relations(
              createChild(CertificateRelationType.REPLACED, CertificateStatus.LOCKED_REVOKED)
          )
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(Collections.EMPTY_LIST, actualStatuses);
    }

    @Test
    void shallConvertToReplacedIfCertificateIsComplemented() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(OLD_DATE)
          .relations(
              createChild(CertificateRelationType.COMPLEMENTED)
          )
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(List.of(CertificateStatusType.REPLACED), actualStatuses);
    }

    @Test
    void shallReturnEmptyListIfCertificateIsNotReplaced() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(OLD_DATE)
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(Collections.emptyList(), actualStatuses);
    }


    @Test
    void shallConvertToReplacedIfBothNewAndReplaced() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(LAST_DAY_AS_NEW)
          .relations(
              createChild(CertificateRelationType.REPLACED)
          )
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(List.of(CertificateStatusType.REPLACED), actualStatuses);
    }

    @Test
    void shallConvertToReplacedIfBothReplacedAndSentToRecipient() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(OLD_DATE)
          .recipient(
              createRecipient(RECIPIENT_ID, LocalDateTime.now())
          )
          .relations(
              createChild(CertificateRelationType.REPLACED)
          )
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(List.of(CertificateStatusType.REPLACED), actualStatuses);
    }

    @Test
    void shallConvertToReplacedIfBothComplementedAndSentToRecipient() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(OLD_DATE)
          .recipient(
              createRecipient(RECIPIENT_ID, LocalDateTime.now())
          )
          .relations(
              createChild(CertificateRelationType.COMPLEMENTED)
          )
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(List.of(CertificateStatusType.REPLACED), actualStatuses);
    }

    @Test
    void shallConvertToReplacedIfBothReplacedAndNotSentToRecipient() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(OLD_DATE)
          .recipient(
              createRecipient(RECIPIENT_ID, null)
          )
          .relations(
              createChild(CertificateRelationType.REPLACED)
          )
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(List.of(CertificateStatusType.REPLACED), actualStatuses);
    }

    @Test
    void shallConvertToReplacedIfBothComplementedAndNotSentToRecipient() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(OLD_DATE)
          .recipient(
              createRecipient(RECIPIENT_ID, null)
          )
          .relations(
              createChild(CertificateRelationType.COMPLEMENTED)
          )
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(List.of(CertificateStatusType.REPLACED), actualStatuses);
    }
  }

  @Nested
  class MultipleStatuses {

    @Test
    void shallConvertToNewAndSentIfBothNewAndSent() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(NEW_DATE)
          .recipient(
              createRecipient(RECIPIENT_ID, LocalDateTime.now())
          )
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(List.of(CertificateStatusType.NEW, CertificateStatusType.SENT), actualStatuses);
    }

    @Test
    void shallConvertToNewAndNotSentIfBothNewAndNotSent() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .created(NEW_DATE)
          .recipient(
              createRecipient(RECIPIENT_ID, null)
          )
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(List.of(CertificateStatusType.NEW, CertificateStatusType.NOT_SENT),
          actualStatuses);
    }

    @Test
    void shallConvertToNewAndSentIfBothNewNotSentAndSent() {
      final var metadataDTO = CertificateMetadataDTO.builder()
          .recipient(
              createRecipient(RECIPIENT_ID, null)
          )
          .recipient(
              createRecipient(RECIPIENT_ID, LocalDateTime.now())
          )
          .created(NEW_DATE)
          .build();

      final var actualStatuses = statusConverter.convert(metadataDTO);
      assertEquals(List.of(CertificateStatusType.NEW, CertificateStatusType.SENT), actualStatuses);

    }
  }

  private CertificateRelations createChild(CertificateRelationType type) {
    return createChild(type, CertificateStatus.SIGNED);
  }

  private CertificateRelations createChild(CertificateRelationType type,
      CertificateStatus certificateStatus) {
    return CertificateRelations.builder()
        .children(
            new CertificateRelation[]{
                CertificateRelation.builder()
                    .type(type)
                    .certificateId("id")
                    .created(LocalDateTime.now())
                    .status(certificateStatus)
                    .build()}
        )
        .build();
  }

  private CertificateRecipient createRecipient(String id, LocalDateTime sentDate) {
    return CertificateRecipient.builder()
        .id(id)
        .sent(sentDate)
        .build();
  }
}