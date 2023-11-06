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
import se.inera.intyg.minaintyg.certificate.dto.CertificateListRequestDTO;
import se.inera.intyg.minaintyg.certificate.service.GetCertificateService;
import se.inera.intyg.minaintyg.certificate.service.ListCertificatesService;
import se.inera.intyg.minaintyg.certificate.service.SendCertificateService;
import se.inera.intyg.minaintyg.certificate.service.dto.FormattedCertificate;
import se.inera.intyg.minaintyg.certificate.service.dto.FormattedCertificateCategory;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateResponse;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesResponse;
import se.inera.intyg.minaintyg.certificate.service.dto.SendCertificateRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateListItem;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;
import se.inera.intyg.minaintyg.integration.api.certificate.model.ResourceLink;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;

@ExtendWith(MockitoExtension.class)
class CertificateControllerTest {

  private static final List<String> YEARS = List.of("2020");
  private static final List<String> UNITS = List.of("unit1");
  private static final List<String> TYPES = List.of("lisjp");
  private static final String CERTIFICATE_ID = "ID";
  private static final List<CertificateStatusType> STATUSES = List.of(CertificateStatusType.SENT);
  private static final List<CertificateListItem> CERTIFICATE_LIST_ITEMS = List.of(
      CertificateListItem.builder().build());

  @Mock
  ListCertificatesService listCertificatesService;
  @Mock
  GetCertificateService getCertificateService;
  @Mock
  SendCertificateService sendCertificateService;

  @InjectMocks
  CertificateController certificateController;

  @Nested
  class ListCertificates {

    @BeforeEach
    void setup() {
      final var response =
          ListCertificatesResponse
              .builder()
              .content(CERTIFICATE_LIST_ITEMS)
              .build();

      when(listCertificatesService.get(any())).thenReturn(response);
    }

    @Nested
    class Request {

      CertificateListRequestDTO request = CertificateListRequestDTO
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
            CertificateListRequestDTO.builder().build());

        assertEquals(CERTIFICATE_LIST_ITEMS, response.getContent());
      }
    }
  }

  @Nested
  class GetCertificate {

    final GetCertificateResponse expectedResponse = GetCertificateResponse
        .builder()
        .certificate(
            FormattedCertificate
                .builder()
                .metadata(CertificateMetadata.builder().build())
                .content(List.of(FormattedCertificateCategory.builder().build()))
                .build()
        )
        .resourceLinks(List.of(ResourceLink.builder().build()))
        .build();

    @BeforeEach
    void setup() {
      when(getCertificateService.get(any(GetCertificateRequest.class)))
          .thenReturn(expectedResponse);
    }

    @Test
    void shouldCallServiceWithCertificateId() {
      certificateController.getCertificate(CERTIFICATE_ID);
      final var captor = ArgumentCaptor.forClass(GetCertificateRequest.class);

      verify(getCertificateService).get(captor.capture());

      assertEquals(CERTIFICATE_ID, captor.getValue().getCertificateId());
    }

    @Nested
    class Response {

      @Test
      void shouldSetCertificate() {
        final var response = certificateController.getCertificate(CERTIFICATE_ID);

        assertEquals(expectedResponse.getCertificate(), response.getCertificate());
      }

      @Test
      void shouldSetResourceLinks() {
        final var response = certificateController.getCertificate(CERTIFICATE_ID);

        assertEquals(expectedResponse.getResourceLinks(), response.getResourceLinks());
      }
    }
  }

  @Nested
  class SendCertificate {

    @Test
    void shouldCallServiceWithCertificateId() {
      certificateController.sendCertificateToRecipient(CERTIFICATE_ID);
      final var captor = ArgumentCaptor.forClass(SendCertificateRequest.class);

      verify(sendCertificateService).send(captor.capture());
      assertEquals(CERTIFICATE_ID, captor.getValue().getCertificateId());
    }
  }
}