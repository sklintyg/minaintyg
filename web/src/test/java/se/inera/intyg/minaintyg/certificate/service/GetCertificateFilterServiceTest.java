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
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateUnit;

@ExtendWith(MockitoExtension.class)
class GetCertificateFilterServiceTest {

  private static final Certificate CERTIFICATE = Certificate
      .builder()
      .issued(LocalDateTime.now())
      .type(
          CertificateType
              .builder()
              .name("NAME")
              .id("ID")
              .build()
      )
      .unit(
          CertificateUnit
              .builder()
              .id("UNIT_ID")
              .name("UNIT_NAME")
              .build()
      )
      .build();
  @Mock
  ListCertificatesService listCertificatesService;
  @InjectMocks
  GetCertificateFilterService getCertificateFilterService;

  @BeforeEach
  void setup() {
    when(listCertificatesService.get(any(ListCertificatesRequest.class))).thenReturn(
        ListCertificatesResponse
            .builder()
            .content(List.of(CERTIFICATE))
            .build()
    );
  }


  @Test
  void shouldSetSentStatus() {
    final var result = getCertificateFilterService.get();

    assertEquals(CertificateStatusType.SENT, result.getStatuses().get(0));
  }

  @Test
  void shouldSetNotSentStatus() {
    final var result = getCertificateFilterService.get();

    assertEquals(CertificateStatusType.NOT_SENT, result.getStatuses().get(1));
  }

  @Test
  void shouldSetUnits() {
    final var result = getCertificateFilterService.get();

    assertEquals(CERTIFICATE.getUnit(), result.getUnits().get(0));
  }

  @Test
  void shouldSetCertificateTypeName() {
    final var result = getCertificateFilterService.get();

    assertEquals(CERTIFICATE.getType().getName(), result.getCertificateTypes().get(0).getName());
  }

  @Test
  void shouldSetCertificateTypeId() {
    final var result = getCertificateFilterService.get();

    assertEquals(CERTIFICATE.getType().getId(), result.getCertificateTypes().get(0).getId());
  }

  @Test
  void shouldSetYears() {
    final var result = getCertificateFilterService.get();

    assertEquals(Year.now().toString(), result.getYears().get(0));
  }
}