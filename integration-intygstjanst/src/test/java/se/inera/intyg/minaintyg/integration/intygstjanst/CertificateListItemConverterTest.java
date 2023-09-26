package se.inera.intyg.minaintyg.integration.intygstjanst;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateEvent;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateIssuerDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRecipientDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateRelationDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateSummaryDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateTypeDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateUnitDTO;


@ExtendWith(MockitoExtension.class)
class CertificateListItemConverterTest {

  private static final String TYPE_ID = "typeId";
  private static final String TYPE_NAME = "typeName";
  private static final String CERTIFICATE_ID = "certificateId";
  private static final String ISSUER_NAME = "IssuerName";
  private static final String TYPE_VERSION = "typeVersion";
  private static final String SUMMARY_VALUE = "summaryValue";
  private static final String SUMMARY_LABEL = "summaryLabel";
  private static final String UNIT_NAME = "unitName";
  private static final String UNIT_ID = "unitId";
  private static final LocalDateTime ISSUED = LocalDateTime.now();
  private static final CertificateRecipientDTO RECIPIENT = CertificateRecipientDTO.builder()
      .build();
  private static final List<CertificateRelationDTO> RELATIONS = Collections.emptyList();
  private static final CertificateDTO ORIGINAL_CERTIFICATE = CertificateDTO
      .builder()
      .id(CERTIFICATE_ID)
      .type(CertificateTypeDTO
          .builder()
          .id(TYPE_ID)
          .name(TYPE_NAME)
          .version(TYPE_VERSION)
          .build())
      .issued(ISSUED)
      .issuer(CertificateIssuerDTO
          .builder()
          .name(ISSUER_NAME)
          .build())
      .recipient(RECIPIENT)
      .summary(
          CertificateSummaryDTO
              .builder()
              .label(SUMMARY_LABEL)
              .value(SUMMARY_VALUE)
              .build()
      )
      .unit(CertificateUnitDTO
          .builder()
          .name(UNIT_NAME)
          .id(UNIT_ID)
          .build())
      .relations(RELATIONS)
      .build();

  @Mock
  CertificateEventService certificateEventService;
  @Mock
  CertificateStatusService certificateStatusService;
  @InjectMocks
  CertificateConverter certificateConverter;

  @Test
  void shouldConvertCertificateId() {
    final var response = certificateConverter.convert(ORIGINAL_CERTIFICATE);

    assertEquals(CERTIFICATE_ID, response.getId());
  }

  @Test
  void shouldConvertTypeId() {
    final var response = certificateConverter.convert(ORIGINAL_CERTIFICATE);

    assertEquals(TYPE_ID, response.getType().getId());
  }

  @Test
  void shouldConvertTypeName() {
    final var response = certificateConverter.convert(ORIGINAL_CERTIFICATE);

    assertEquals(TYPE_NAME, response.getType().getName());
  }

  @Test
  void shouldConvertTypeVersion() {
    final var response = certificateConverter.convert(ORIGINAL_CERTIFICATE);

    assertEquals(TYPE_VERSION, response.getType().getVersion());
  }

  @Test
  void shouldConvertSummaryValue() {
    final var response = certificateConverter.convert(ORIGINAL_CERTIFICATE);

    assertEquals(SUMMARY_VALUE, response.getSummary().getValue());
  }

  @Test
  void shouldConvertSummaryLabel() {
    final var response = certificateConverter.convert(ORIGINAL_CERTIFICATE);

    assertEquals(SUMMARY_LABEL, response.getSummary().getLabel());
  }

  @Test
  void shouldConvertIssuerName() {
    final var response = certificateConverter.convert(ORIGINAL_CERTIFICATE);

    assertEquals(ISSUER_NAME, response.getIssuer().getName());
  }

  @Test
  void shouldConvertUnitId() {
    final var response = certificateConverter.convert(ORIGINAL_CERTIFICATE);

    assertEquals(UNIT_ID, response.getUnit().getId());
  }

  @Test
  void shouldConvertUnitName() {
    final var response = certificateConverter.convert(ORIGINAL_CERTIFICATE);

    assertEquals(UNIT_NAME, response.getUnit().getName());
  }

  @Test
  void shouldConvertIssued() {
    final var response = certificateConverter.convert(ORIGINAL_CERTIFICATE);

    assertEquals(ISSUED, response.getIssued());
  }

  @Nested
  class ConvertStatuses {

    List<CertificateStatusType> statuses = Collections.emptyList();

    @BeforeEach
    void setup() {
      Mockito.when(certificateStatusService.get(
              anyList(),
              any(CertificateRecipientDTO.class),
              any(LocalDateTime.class)
          )
      ).thenReturn(statuses);
    }

    @Test
    void shouldSendRelationsAsArgument() {
      certificateConverter.convert(ORIGINAL_CERTIFICATE);

      final var captor = ArgumentCaptor.forClass(List.class);

      verify(certificateStatusService)
          .get(captor.capture(), any(CertificateRecipientDTO.class), any(LocalDateTime.class));

      assertEquals(RELATIONS, captor.getValue());
    }

    @Test
    void shouldSendRecipientAsArgument() {
      certificateConverter.convert(ORIGINAL_CERTIFICATE);

      final var captor = ArgumentCaptor.forClass(CertificateRecipientDTO.class);

      verify(certificateStatusService)
          .get(anyList(), captor.capture(), any(LocalDateTime.class));

      assertEquals(RECIPIENT, captor.getValue());
    }

    @Test
    void shouldSendIssuedAsArgument() {
      certificateConverter.convert(ORIGINAL_CERTIFICATE);

      final var captor = ArgumentCaptor.forClass(LocalDateTime.class);

      verify(certificateStatusService)
          .get(anyList(), any(CertificateRecipientDTO.class), captor.capture());

      assertEquals(ISSUED, captor.getValue());
    }

    @Test
    void shouldSetStatuses() {
      final var response = certificateConverter.convert(ORIGINAL_CERTIFICATE);

      assertEquals(statuses, response.getStatuses());
    }
  }

  @Nested
  class ConvertEvents {

    List<CertificateEvent> events = Collections.emptyList();

    @BeforeEach
    void setup() {
      Mockito.when(certificateEventService.get(
              anyList(),
              any(CertificateRecipientDTO.class)
          )
      ).thenReturn(events);
    }

    @Test
    void shouldSendRelationsAsArgument() {
      certificateConverter.convert(ORIGINAL_CERTIFICATE);

      final var captor = ArgumentCaptor.forClass(List.class);

      verify(certificateEventService)
          .get(captor.capture(), any(CertificateRecipientDTO.class));

      assertEquals(RELATIONS, captor.getValue());
    }

    @Test
    void shouldSendRecipientAsArgument() {
      certificateConverter.convert(ORIGINAL_CERTIFICATE);

      final var captor = ArgumentCaptor.forClass(CertificateRecipientDTO.class);

      verify(certificateEventService)
          .get(anyList(), captor.capture());

      assertEquals(RECIPIENT, captor.getValue());
    }

    @Test
    void shouldSetStatuses() {
      final var response = certificateConverter.convert(ORIGINAL_CERTIFICATE);

      assertEquals(events, response.getEvents());
    }
  }

}