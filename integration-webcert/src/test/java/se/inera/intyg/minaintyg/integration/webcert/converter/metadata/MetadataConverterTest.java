package se.inera.intyg.minaintyg.integration.webcert.converter.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateEvent;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.Staff;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.Unit;

@ExtendWith(MockitoExtension.class)
class MetadataConverterTest {

  public static final String ID = "id";
  public static final LocalDateTime ISSUED = LocalDateTime.now();
  public static final String ISSUED_NAME = "issuedName";
  public static final String TYPE_ID = "typeId";
  public static final String TYPE_NAME = "typeName";
  public static final String TYPE_VERSION = "typeVersion";
  public static final String UNIT_ID = "unitId";
  public static final String UNIT_NAME = "unitName";
  public static final String RECIPIENT_NAME = "recipientName";
  public static final String RECIPIENT_ID = "recipientId";
  public static final LocalDateTime RECIPIENT_SENT = LocalDateTime.now();
  @Mock
  private EventConverter eventConverter;
  @Mock
  private StatusConverter statusConverter;
  @InjectMocks
  private MetadataConverter metadataConverter;

  final CertificateMetadataDTO metadataDTO = CertificateMetadataDTO.builder()
      .id(ID)
      .type(TYPE_ID)
      .typeName(TYPE_NAME)
      .typeVersion(TYPE_VERSION)
      .issuedBy(Staff.builder()
          .fullName(ISSUED_NAME)
          .build())
      .unit(Unit
          .builder()
          .unitId(UNIT_ID)
          .unitName(UNIT_NAME)
          .build())
      .created(ISSUED)
      .recipient(CertificateRecipient.builder()
          .id(RECIPIENT_ID)
          .name(RECIPIENT_NAME)
          .sent(RECIPIENT_SENT)
          .build())
      .build();

  @Test
  void shallConvertCertificateId() {
    final var actualMetadata = metadataConverter.convert(metadataDTO);
    assertEquals(ID, actualMetadata.getId());
  }

  @Test
  void shallConvertTypeId() {
    final var actualMetadata = metadataConverter.convert(metadataDTO);
    assertEquals(TYPE_ID, actualMetadata.getType().getId());
  }

  @Test
  void shallConvertTypeName() {
    final var actualMetadata = metadataConverter.convert(metadataDTO);
    assertEquals(TYPE_NAME, actualMetadata.getType().getName());
  }

  @Test
  void shallConvertTypeVersion() {
    final var actualMetadata = metadataConverter.convert(metadataDTO);
    assertEquals(TYPE_VERSION, actualMetadata.getType().getVersion());
  }

  @Test
  void shallConvertIssuer() {
    final var actualMetadata = metadataConverter.convert(metadataDTO);
    assertEquals(ISSUED_NAME, actualMetadata.getIssuer().getName());
  }

  @Test
  void shallConvertUnitId() {
    final var actualMetadata = metadataConverter.convert(metadataDTO);
    assertEquals(UNIT_ID, actualMetadata.getUnit().getId());
  }

  @Test
  void shallConvertUnitName() {
    final var actualMetadata = metadataConverter.convert(metadataDTO);
    assertEquals(UNIT_NAME, actualMetadata.getUnit().getName());
  }

  @Test
  void ShallConvertEvents() {
    final var expectedMetadata = List.of(CertificateEvent.builder().build());
    doReturn(expectedMetadata).when(eventConverter).convert(metadataDTO);

    final var actualMetadata = metadataConverter.convert(metadataDTO);
    assertEquals(expectedMetadata, actualMetadata.getEvents());
  }

  @Test
  void ShallConvertStatuses() {
    final var expectedMetadata = List.of(CertificateStatusType.SENT);
    doReturn(expectedMetadata).when(statusConverter).convert(metadataDTO);

    final var actualMetadata = metadataConverter.convert(metadataDTO);
    assertEquals(expectedMetadata, actualMetadata.getStatuses());
  }

  @Test
  void shallConvertIssued() {
    final var actualMetadata = metadataConverter.convert(metadataDTO);
    assertEquals(ISSUED, actualMetadata.getIssued());
  }

  @Test
  void shallConvertRecipientId() {
    final var actualMetadata = metadataConverter.convert(metadataDTO);
    assertEquals(RECIPIENT_ID, actualMetadata.getRecipient().getId());
  }

  @Test
  void shallConvertRecipientName() {
    final var actualMetadata = metadataConverter.convert(metadataDTO);
    assertEquals(RECIPIENT_NAME, actualMetadata.getRecipient().getName());
  }

  @Test
  void shallConvertRecipientSent() {
    final var actualMetadata = metadataConverter.convert(metadataDTO);
    assertEquals(RECIPIENT_SENT, actualMetadata.getRecipient().getSent());
  }

  @Test
  void shallReturnNullIfNoRecipient() {
    final CertificateMetadataDTO metadataDTONoRecipient = CertificateMetadataDTO.builder()
        .id(ID)
        .type(TYPE_ID)
        .typeName(TYPE_NAME)
        .typeVersion(TYPE_VERSION)
        .issuedBy(Staff.builder()
            .fullName(ISSUED_NAME)
            .build())
        .unit(Unit
            .builder()
            .unitId(UNIT_ID)
            .unitName(UNIT_NAME)
            .build())
        .created(ISSUED)
        .build();

    final var actualMetadata = metadataConverter.convert(metadataDTONoRecipient);
    assertNull(actualMetadata.getRecipient());
  }
}