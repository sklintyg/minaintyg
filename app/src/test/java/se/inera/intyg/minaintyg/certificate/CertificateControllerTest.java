package se.inera.intyg.minaintyg.certificate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HexFormat;
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
import se.inera.intyg.minaintyg.certificate.dto.PrintCertificateRequestDTO;
import se.inera.intyg.minaintyg.certificate.service.GetCertificateService;
import se.inera.intyg.minaintyg.certificate.service.ListCertificatesService;
import se.inera.intyg.minaintyg.certificate.service.PrintCertificateService;
import se.inera.intyg.minaintyg.certificate.service.SendCertificateService;
import se.inera.intyg.minaintyg.certificate.service.dto.FormattedCertificate;
import se.inera.intyg.minaintyg.certificate.service.dto.FormattedCertificateCategory;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateResponse;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesResponse;
import se.inera.intyg.minaintyg.certificate.service.dto.PrintCertificateRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.PrintCertificateResponse;
import se.inera.intyg.minaintyg.certificate.service.dto.SendCertificateRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateListItem;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;
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
  @Mock
  PrintCertificateService printCertificateService;

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

  @Nested
  class PrintCertificate {

    private static final PrintCertificateRequestDTO REQUEST = PrintCertificateRequestDTO.builder()
        .customizationId("C_ID")
        .build();

    @BeforeEach
    void setup() {
      when(printCertificateService.print(any(PrintCertificateRequest.class)))
          .thenReturn(EXPECTED_RESPONSE);
    }

    private static final PrintCertificateResponse EXPECTED_RESPONSE = PrintCertificateResponse
        .builder()
        .filename("PRINT")
        .pdfData(HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d"))
        .build();

    @Test
    void shouldUseCertificateIdInRequest() {
      certificateController.printCertificate(CERTIFICATE_ID, REQUEST);
      final var captor = ArgumentCaptor.forClass(PrintCertificateRequest.class);

      verify(printCertificateService).print(captor.capture());
      assertEquals(CERTIFICATE_ID, captor.getValue().getCertificateId());
    }

    @Test
    void shouldUseCustomizationIdInRequest() {
      certificateController.printCertificate(CERTIFICATE_ID, REQUEST);
      final var captor = ArgumentCaptor.forClass(PrintCertificateRequest.class);

      verify(printCertificateService).print(captor.capture());
      assertEquals(REQUEST.getCustomizationId(), captor.getValue().getCustomizationId());
    }

    @Test
    void shouldReturnFilenameReturnedFromService() {
      final var response = certificateController.printCertificate(CERTIFICATE_ID, REQUEST);

      assertEquals(EXPECTED_RESPONSE.getFilename(), response.getFilename());
    }

    @Test
    void shouldReturnPdfDataReturnedFromService() {
      final var response = certificateController.printCertificate(CERTIFICATE_ID, REQUEST);

      assertEquals(EXPECTED_RESPONSE.getPdfData(), response.getPdfData());
    }
  }
}