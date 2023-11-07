package se.inera.intyg.minaintyg.certificate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HexFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.certificate.service.dto.PrintCertificateRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationService;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;

@ExtendWith(MockitoExtension.class)
class PrintCertificateServiceTest {

  public static final PrintCertificateRequest REQUEST = PrintCertificateRequest.builder()
      .certificateId("ID").build();
  public static final PrintCertificateIntegrationResponse EXPECTED_RESPONSE = PrintCertificateIntegrationResponse
      .builder()
      .filename("PRINTABLE")
      .pdfData(HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d"))
      .build();
  @Mock
  PrintCertificateIntegrationService printCertificateIntegrationService;
  @Mock
  MonitoringLogService monitorLogService;
  @InjectMocks
  PrintCertificateService printCertificateService;

  @BeforeEach
  void setup() {
    when(printCertificateIntegrationService.print(any(PrintCertificateIntegrationRequest.class)))
        .thenReturn(EXPECTED_RESPONSE);
  }

  @Test
  void shouldSendCertificateIdInRequest() {
    final var captor = ArgumentCaptor.forClass(PrintCertificateIntegrationRequest.class);

    printCertificateService.print(REQUEST);

    verify(printCertificateIntegrationService).print(captor.capture());
    assertEquals(REQUEST.getCertificateId(), captor.getValue().getCertificateId());
  }

  @Test
  void shouldSendCustomizationIdInRequest() {
    final var captor = ArgumentCaptor.forClass(PrintCertificateIntegrationRequest.class);

    printCertificateService.print(REQUEST);

    verify(printCertificateIntegrationService).print(captor.capture());
    assertEquals(REQUEST.getCustomizationId(), captor.getValue().getCustomizationId());
  }

  @Test
  void shouldReturnFilenameInResponse() {
    final var response = printCertificateService.print(REQUEST);

    assertEquals(EXPECTED_RESPONSE.getFilename(), response.getFilename());
  }

  @Test
  void shouldReturnPdfDataInResponse() {
    final var response = printCertificateService.print(REQUEST);

    assertEquals(EXPECTED_RESPONSE.getPdfData(), response.getPdfData());
  }

  @Test
  void shouldPerformMonitorLogging() {
    final var captor = ArgumentCaptor.forClass(String.class);

    printCertificateService.print(REQUEST);

    verify(monitorLogService, times(1)).logCertificatePrinted(captor.capture());
    assertEquals(REQUEST.getCertificateId(), captor.getValue());
  }
}