package se.inera.intyg.minaintyg.certificate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.certificate.service.ListCertificatesService;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateStatusType;

@ExtendWith(MockitoExtension.class)
class CertificateControllerTest {

  private static final List<String> YEARS = List.of("2020");
  private static final List<String> UNITS = List.of("unit1");
  private static final List<String> TYPES = List.of("lisjp");
  private static final List<CertificateStatusType> STATUSES = List.of(CertificateStatusType.SENT);
  private static final List<Certificate> certificates = List.of(Certificate.builder().build());

  @Mock
  ListCertificatesService listCertificatesService;

  @InjectMocks
  CertificateController certificateController;

  @BeforeEach
  void setup() {
    final var response =
        ListCertificatesResponse
            .builder()
            .content(certificates)
            .build();

    when(listCertificatesService.get(any())).thenReturn(response);
  }

  @Nested
  class Request {

    CertificatesRequest request = CertificatesRequest
        .builder()
        .years(YEARS)
        .units(UNITS)
        .certificateTypes(TYPES)
        .statuses(STATUSES)
        .build();

    @Test
    void shouldSendYears() {
      certificateController.listCertificates(request);

      final var captor = ArgumentCaptor.forClass(ListCertificatesRequest.class);

      verify(listCertificatesService).get(captor.capture());
      assertEquals(YEARS, captor.getValue().getYears());
    }

    @Test
    void shouldSendUnits() {
      certificateController.listCertificates(request);

      final var captor = ArgumentCaptor.forClass(ListCertificatesRequest.class);

      verify(listCertificatesService).get(captor.capture());
      assertEquals(UNITS, captor.getValue().getUnits());
    }

    @Test
    void shouldSendCertificateTypes() {
      certificateController.listCertificates(request);

      final var captor = ArgumentCaptor.forClass(ListCertificatesRequest.class);

      verify(listCertificatesService).get(captor.capture());
      assertEquals(TYPES, captor.getValue().getCertificateTypes());
    }

    @Test
    void shouldSendStatuses() {
      certificateController.listCertificates(request);

      final var captor = ArgumentCaptor.forClass(ListCertificatesRequest.class);

      verify(listCertificatesService).get(captor.capture());
      assertEquals(STATUSES, captor.getValue().getStatuses());
    }
  }

  @Nested
  class Response {

    @Test
    void shouldSetContent() {
      final var response = certificateController.listCertificates(
          CertificatesRequest.builder().build());

      assertEquals(certificates, response.getContent());
    }
  }
}