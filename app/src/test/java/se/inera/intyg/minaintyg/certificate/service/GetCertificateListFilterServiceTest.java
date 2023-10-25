package se.inera.intyg.minaintyg.certificate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateListItem;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateTypeFilter;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateUnit;

@ExtendWith(MockitoExtension.class)
class GetCertificateListFilterServiceTest {

  private static final String UNIT_ID = "UNIT_ID";
  private static final String A_UNIT_NAME = "A_UNIT_NAME";
  private static final String B_UNIT_NAME = "B_UNIT_NAME";
  private static final String A_TYPE_NAME = "A_TYPE_NAME";
  private static final String B_TYPE_NAME = "B_TYPE_NAME";
  private static final String ID = "ID";
  private static final CertificateListItem CERTIFICATE_LIST_ITEM_1 = CertificateListItem
      .builder()
      .issued(LocalDateTime.now())
      .type(
          CertificateType
              .builder()
              .name(A_TYPE_NAME)
              .id(ID).build()
      )
      .unit(
          CertificateUnit
              .builder()
              .id(UNIT_ID)
              .name(A_UNIT_NAME)
              .build()
      )
      .build();

  private static final CertificateListItem CERTIFICATE_LIST_ITEM_2 = CertificateListItem
      .builder()
      .issued(LocalDateTime.now().minusYears(10))
      .type(
          CertificateType
              .builder()
              .name(B_TYPE_NAME)
              .id(ID).build()
      )
      .unit(
          CertificateUnit
              .builder()
              .id(UNIT_ID)
              .name(B_UNIT_NAME)
              .build()
      )
      .build();
  @Mock
  ListCertificatesService listCertificatesService;
  @InjectMocks
  GetCertificateListFilterService getCertificateListFilterService;

  @BeforeEach
  void setup() {
    when(listCertificatesService.get(any(ListCertificatesRequest.class))).thenReturn(
        ListCertificatesResponse
            .builder()
            .content(List.of(CERTIFICATE_LIST_ITEM_2, CERTIFICATE_LIST_ITEM_1))
            .build()
    );
  }

  @Test
  void shouldSetSentStatus() {
    final var result = getCertificateListFilterService.get();

    assertEquals(CertificateStatusType.SENT, result.getStatuses().get(1));
  }

  @Test
  void shouldSetNotSentStatus() {
    final var result = getCertificateListFilterService.get();

    assertEquals(CertificateStatusType.NOT_SENT, result.getStatuses().get(0));
  }

  @Test
  void shouldSetUnits() {
    final var result = getCertificateListFilterService.get();

    assertEquals(CERTIFICATE_LIST_ITEM_1.getUnit(), result.getUnits().get(0));
  }

  @Test
  void shouldSetCertificateTypeName() {
    final var result = getCertificateListFilterService.get();

    assertEquals(CERTIFICATE_LIST_ITEM_1.getType().getName(),
        result.getCertificateTypes().get(0).getName());
  }

  @Test
  void shouldSetCertificateTypeId() {
    final var result = getCertificateListFilterService.get();

    assertEquals(CERTIFICATE_LIST_ITEM_1.getType().getId(),
        result.getCertificateTypes().get(0).getId());
  }

  @Test
  void shouldSetYears() {
    final var result = getCertificateListFilterService.get();

    assertEquals(Year.now().toString(), result.getYears().get(0));
  }

  @Test
  void shouldSetTotal() {
    final var result = getCertificateListFilterService.get();

    assertEquals(2, result.getTotal());
  }

  @Test
  void shouldSortTypeNamesAlphabetically() {
    final var result = getCertificateListFilterService.get();

    final var expectedResult = List.of(CertificateTypeFilter.builder()
            .name(A_TYPE_NAME)
            .id(ID)
            .build(),
        CertificateTypeFilter.builder()
            .name(B_TYPE_NAME)
            .id(ID)
            .build()
    );

    assertEquals(expectedResult, result.getCertificateTypes());
  }

  @Test
  void shouldSortUnitsAlphabetically() {
    final var result = getCertificateListFilterService.get();

    final var expectedResult = List.of(CertificateUnit
            .builder()
            .id(UNIT_ID)
            .name(A_UNIT_NAME)
            .build(),
        CertificateUnit
            .builder()
            .id(UNIT_ID)
            .name(B_UNIT_NAME)
            .build()
    );

    assertEquals(expectedResult, result.getUnits());
  }

  @Test
  void shouldSortStatusesAlphabetically() {
    final var result = getCertificateListFilterService.get();

    assertEquals(List.of(CertificateStatusType.NOT_SENT, CertificateStatusType.SENT),
        result.getStatuses());
  }

  @Test
  void shouldSortYearsDescending() {
    final var result = getCertificateListFilterService.get();

    final var expectedResult = List.of(String.valueOf(LocalDateTime.now().getYear()),
        String.valueOf(LocalDateTime.now().minusYears(10).getYear()));

    assertEquals(expectedResult, result.getYears());
  }
}