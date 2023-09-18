package se.inera.intyg.minaintyg.certificate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.auth.LoginMethod;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificatesService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificatesRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificatesResponse;
import se.inera.intyg.minaintyg.user.MinaIntygUserService;

@ExtendWith(MockitoExtension.class)
class ListCertificatesServiceTest {

  private static final String PATIENT_ID = "PatientId";
  private static final List<String> YEARS = List.of("2020");
  private static final List<String> UNITS = List.of("unit1");
  private static final List<String> TYPES = List.of("lisjp");
  private static final List<CertificateStatusType> STATUSES = List.of(CertificateStatusType.SENT);
  private static final Certificate certificate = Certificate.builder().build();

  @Mock
  GetCertificatesService getCertificatesService;

  @Mock
  MinaIntygUserService minaIntygUserService;

  @InjectMocks
  ListCertificatesService listCertificatesService;

  @BeforeEach
  void setup() {
    when(minaIntygUserService.getUser()).thenReturn(
        Optional.of(
            new MinaIntygUser(Collections.emptySet(), PATIENT_ID, "name", LoginMethod.FAKE)));

    when(getCertificatesService.get(any())).thenReturn(
        CertificatesResponse
            .builder()
            .content(List.of(certificate))
            .build()
    );
  }

  @Nested
  class Request {

    ListCertificatesRequest request = ListCertificatesRequest
        .builder()
        .years(YEARS)
        .units(UNITS)
        .certificateTypes(TYPES)
        .statuses(STATUSES)
        .build();

    @Test
    void shouldSendPatientId() {
      listCertificatesService.get(request);

      final var captor = ArgumentCaptor.forClass(CertificatesRequest.class);

      verify(getCertificatesService).get(captor.capture());
      assertEquals(PATIENT_ID, captor.getValue().getPatientId());
    }

    @Test
    void shouldSendYears() {
      listCertificatesService.get(request);

      final var captor = ArgumentCaptor.forClass(CertificatesRequest.class);

      verify(getCertificatesService).get(captor.capture());
      assertEquals(YEARS, captor.getValue().getYears());
    }

    @Test
    void shouldSendUnits() {
      listCertificatesService.get(request);

      final var captor = ArgumentCaptor.forClass(CertificatesRequest.class);

      verify(getCertificatesService).get(captor.capture());
      assertEquals(UNITS, captor.getValue().getUnits());
    }

    @Test
    void shouldSendCertificateTypes() {
      listCertificatesService.get(request);

      final var captor = ArgumentCaptor.forClass(CertificatesRequest.class);

      verify(getCertificatesService).get(captor.capture());
      assertEquals(TYPES, captor.getValue().getCertificateTypes());
    }

    @Test
    void shouldSendStatuses() {
      listCertificatesService.get(request);

      final var captor = ArgumentCaptor.forClass(CertificatesRequest.class);

      verify(getCertificatesService).get(captor.capture());
      assertEquals(STATUSES, captor.getValue().getStatuses());
    }
  }

  @Nested
  class Response {

    @Test
    void shouldSetCertificateAsContent() {
      final var response = listCertificatesService.get(ListCertificatesRequest.builder().build());

      assertEquals(certificate, response.getContent().get(0));
    }
  }
}