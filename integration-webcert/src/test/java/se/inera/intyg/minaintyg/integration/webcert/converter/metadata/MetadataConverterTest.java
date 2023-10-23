package se.inera.intyg.minaintyg.integration.webcert.converter.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateSummary.builder;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateEvent;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO.CertificateMetadataDTOBuilder;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.CertificateSummary;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.Staff;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.Unit;

@ExtendWith(MockitoExtension.class)
class MetadataConverterTest {

  public static final String ID = "id";
  public static final LocalDateTime ISSUED = LocalDateTime.now();
  public static final String ISSUED_NAME = "issuedName";
  public static final String TYPE_ID = "typeId";
  public static final String TYPE_VERSION = "typeVersion";
  public static final String UNIT_ID = "unitId";
  public static final String UNIT_NAME = "unitName";
  public static final String UNIT_ADRESS = "unitAdress";
  public static final String UNIT_ZIPCODE = "unitZipcode";
  public static final String UNIT_CITY = "unitCity";
  public static final String UNIT_PHONE_NUMBER = "unitPhoneNumber";
  public static final String UNIT_EMAIL = "unitEmail";
  public static final String CARE_UNIT_ID = "careUnitId";
  public static final String CARE_UNIT_NAME = "careUnitName";
  public static final String CARE_UNIT_ADRESS = "careUnitAdress";
  public static final String CARE_UNIT_ZIPCODE = "careUnitZipcode";
  public static final String CARE_UNIT_CITY = "careUnitCity";
  public static final String CARE_UNIT_PHONE_NUMBER = "careUnitPhoneNumber";
  public static final String CARE_UNIT_EMAIL = "careUnitEmail";
  public static final String RECIPIENT_NAME = "recipientName";
  public static final String RECIPIENT_ID = "recipientId";
  public static final LocalDateTime RECIPIENT_SENT = LocalDateTime.now();
  public static final String SUMMARY_LABEL = "summaryLabel";
  public static final String SUMMARY_VALUE = "summaryValue";
  public static final String NAME = "certificateName";

  @Mock
  private EventConverter eventConverter;
  @Mock
  private StatusConverter statusConverter;
  @InjectMocks
  private MetadataConverter metadataConverter;
  private CertificateMetadataDTOBuilder metadataDTO;

  @BeforeEach
  void setup() {
    metadataDTO = CertificateMetadataDTO.builder()
        .id(ID)
        .name(NAME)
        .type(TYPE_ID)
        .typeVersion(TYPE_VERSION)
        .issuedBy(Staff.builder()
            .fullName(ISSUED_NAME)
            .build())
        .unit(Unit.builder()
            .unitId(UNIT_ID)
            .unitName(UNIT_NAME)
            .address(UNIT_ADRESS)
            .zipCode(UNIT_ZIPCODE)
            .city(UNIT_CITY)
            .phoneNumber(UNIT_PHONE_NUMBER)
            .email(UNIT_EMAIL)
            .build())
        .careUnit(Unit.builder()
            .unitId(CARE_UNIT_ID)
            .unitName(CARE_UNIT_NAME)
            .address(CARE_UNIT_ADRESS)
            .zipCode(CARE_UNIT_ZIPCODE)
            .city(CARE_UNIT_CITY)
            .phoneNumber(CARE_UNIT_PHONE_NUMBER)
            .email(CARE_UNIT_EMAIL)
            .build())
        .created(ISSUED)
        .recipient(CertificateRecipient.builder()
            .id(RECIPIENT_ID)
            .name(RECIPIENT_NAME)
            .sent(RECIPIENT_SENT)
            .build())
        .summary(CertificateSummary.builder()
            .label(SUMMARY_LABEL)
            .value(SUMMARY_VALUE)
            .build());
  }

  @Test
  void shallConvertCertificateId() {
    final var actualMetadata = metadataConverter.convert(metadataDTO.build());
    assertEquals(ID, actualMetadata.getId());
  }

  @Test
  void shallConvertIssuer() {
    final var actualMetadata = metadataConverter.convert(metadataDTO.build());
    assertEquals(ISSUED_NAME, actualMetadata.getIssuer().getName());
  }

  @Test
  void shallConvertEvents() {
    final var expectedMetadata = List.of(CertificateEvent.builder().build());
    doReturn(expectedMetadata).when(eventConverter).convert(metadataDTO.build());

    final var actualMetadata = metadataConverter.convert(metadataDTO.build());
    assertEquals(expectedMetadata, actualMetadata.getEvents());
  }

  @Test
  void shallConvertStatuses() {
    final var expectedMetadata = List.of(CertificateStatusType.SENT);
    doReturn(expectedMetadata).when(statusConverter).convert(metadataDTO.build());

    final var actualMetadata = metadataConverter.convert(metadataDTO.build());
    assertEquals(expectedMetadata, actualMetadata.getStatuses());
  }

  @Test
  void shallConvertIssued() {
    final var actualMetadata = metadataConverter.convert(metadataDTO.build());
    assertEquals(ISSUED, actualMetadata.getIssued());
  }

  @Nested
  class ConvertType {

    @Test
    void shallConvertTypeId() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(TYPE_ID, actualMetadata.getType().getId());
    }

    @Test
    void shallConvertTypeName() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(NAME, actualMetadata.getType().getName());
    }

    @Test
    void shallConvertTypeVersion() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(TYPE_VERSION, actualMetadata.getType().getVersion());
    }
  }

  @Nested
  class ConvertUnit {

    @Test
    void shallConvertUnitId() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(UNIT_ID, actualMetadata.getUnit().getId());
    }

    @Test
    void shallConvertUnitName() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(UNIT_NAME, actualMetadata.getUnit().getName());
    }

    @Test
    void shallConvertUnitAdress() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(UNIT_ADRESS, actualMetadata.getUnit().getAddress());
    }

    @Test
    void shallConvertUnitZipcode() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(UNIT_ZIPCODE, actualMetadata.getUnit().getZipCode());
    }

    @Test
    void shallConvertUnitCity() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(UNIT_CITY, actualMetadata.getUnit().getCity());
    }

    @Test
    void shallConvertUnitPhoneNumber() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(UNIT_PHONE_NUMBER, actualMetadata.getUnit().getPhoneNumber());
    }

    @Test
    void shallConvertUnitEmail() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(UNIT_EMAIL, actualMetadata.getUnit().getEmail());
    }
  }

  @Nested
  class ConvertCareUnit {

    @Test
    void shallConvertCareUnitId() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(CARE_UNIT_ID, actualMetadata.getCareUnit().getId());
    }

    @Test
    void shallConvertCareUnitName() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(CARE_UNIT_NAME, actualMetadata.getCareUnit().getName());
    }

    @Test
    void shallConvertCareUnitAdress() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(CARE_UNIT_ADRESS, actualMetadata.getCareUnit().getAddress());
    }

    @Test
    void shallConvertCareUnitZipcode() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(CARE_UNIT_ZIPCODE, actualMetadata.getCareUnit().getZipCode());
    }

    @Test
    void shallConvertCareUnitCity() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(CARE_UNIT_CITY, actualMetadata.getCareUnit().getCity());
    }

    @Test
    void shallConvertCareUnitPhoneNumber() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(CARE_UNIT_PHONE_NUMBER, actualMetadata.getCareUnit().getPhoneNumber());
    }

    @Test
    void shallConvertCareUnitEmail() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(CARE_UNIT_EMAIL, actualMetadata.getCareUnit().getEmail());
    }
  }

  @Nested
  class ConvertRecipient {

    @Test
    void shallConvertRecipientId() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(RECIPIENT_ID, actualMetadata.getRecipient().getId());
    }

    @Test
    void shallConvertRecipientName() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(RECIPIENT_NAME, actualMetadata.getRecipient().getName());
    }

    @Test
    void shallConvertRecipientSent() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(RECIPIENT_SENT, actualMetadata.getRecipient().getSent());
    }

    @Test
    void shallReturnNullIfNoRecipient() {
      metadataDTO.recipient(null);

      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertNull(actualMetadata.getRecipient(),
          "Recipient was %s".formatted(actualMetadata.getRecipient()));
    }
  }

  @Nested
  class ConvertSummary {

    @Test
    void shallConvertSummaryLabel() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(SUMMARY_LABEL, actualMetadata.getSummary().getLabel());
    }

    @Test
    void shallConvertSummaryValue() {
      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(SUMMARY_VALUE, actualMetadata.getSummary().getValue());
    }

    @Test
    void shallReturnNullSummaryLabelIfNoSummaryLabel() {
      metadataDTO.summary(CertificateSummary.builder()
          .label(null)
          .build());

      final var expectedMetadata = builder()
          .label(null)
          .build();

      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(expectedMetadata, actualMetadata.getSummary());
    }

    @Test
    void shallReturnNullSummaryValueIfNoSummaryValue() {
      metadataDTO.summary(CertificateSummary.builder()
          .value(null)
          .build());

      final var expectedMetadata = builder()
          .value(null)
          .build();

      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(expectedMetadata, actualMetadata.getSummary());
    }

    @Test
    void shallReturnSummaryWithNullLabelAndNullValueIfNoSummary() {
      metadataDTO.summary(null);

      final var expectedMetadata = builder()
          .label(null)
          .value(null)
          .build();

      final var actualMetadata = metadataConverter.convert(metadataDTO.build());
      assertEquals(expectedMetadata, actualMetadata.getSummary());
    }
  }
}