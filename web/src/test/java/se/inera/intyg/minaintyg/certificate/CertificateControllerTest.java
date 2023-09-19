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
import se.inera.intyg.minaintyg.certificate.service.GetCertificateFilterService;
import se.inera.intyg.minaintyg.certificate.service.ListCertificatesService;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateFilterResponse;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateTypeFilter;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateUnit;

@ExtendWith(MockitoExtension.class)
class CertificateControllerTest {

  private static final List<String> YEARS = List.of("2020");
  private static final List<String> UNITS = List.of("unit1");
  private static final List<String> TYPES = List.of("lisjp");
  private static final List<CertificateStatusType> STATUSES = List.of(CertificateStatusType.SENT);
  private static final List<Certificate> certificates = List.of(Certificate.builder().build());

  @Mock
  ListCertificatesService listCertificatesService;

  @Mock
  GetCertificateFilterService getCertificateFilterService;

  @InjectMocks
  CertificateController certificateController;

  @Nested
  class ListCertificates {

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

      CertificatesRequestDTO request = CertificatesRequestDTO
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
            CertificatesRequestDTO.builder().build());

        assertEquals(certificates, response.getContent());
      }
    }
  }

  @Nested
  class FilterService {

    private static final GetCertificateFilterResponse EXPECTED_RESPONSE = GetCertificateFilterResponse
        .builder()
        .statuses(List.of(CertificateStatusType.SENT))
        .years(List.of("2020"))
        .certificateTypes(List.of(CertificateTypeFilter.builder().build()))
        .units(List.of(CertificateUnit.builder().build()))
        .build();

    @BeforeEach
    void setup() {
      when(getCertificateFilterService.get()).thenReturn(EXPECTED_RESPONSE);
    }

    @Nested
    class Response {

      @Test
      void shouldSetCertificateTypes() {
        final var response = certificateController.getFilters();

        assertEquals(EXPECTED_RESPONSE.getCertificateTypes(), response.getCertificateTypes());
      }

      @Test
      void shouldSetYears() {
        final var response = certificateController.getFilters();

        assertEquals(EXPECTED_RESPONSE.getYears(), response.getYears());
      }

      @Test
      void shouldSetUnits() {
        final var response = certificateController.getFilters();

        assertEquals(EXPECTED_RESPONSE.getUnits(), response.getUnits());
      }

      @Test
      void shouldSetStatuses() {
        final var response = certificateController.getFilters();

        assertEquals(EXPECTED_RESPONSE.getStatuses(), response.getStatuses());
      }
    }
  }
}