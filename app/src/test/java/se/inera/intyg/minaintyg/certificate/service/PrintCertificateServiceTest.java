package se.inera.intyg.minaintyg.certificate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
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
import se.inera.intyg.minaintyg.certificate.service.dto.FormattedCertificate;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRequest;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateResponse;
import se.inera.intyg.minaintyg.certificate.service.dto.PrintCertificateRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateType;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;

@ExtendWith(MockitoExtension.class)
class PrintCertificateServiceTest {

  public static final PrintCertificateRequest REQUEST = PrintCertificateRequest.builder()
      .certificateId("ID")
      .customizationId("C_ID")
      .build();
  public static final PrintCertificateIntegrationResponse EXPECTED_RESPONSE = PrintCertificateIntegrationResponse
      .builder()
      .filename("PRINTABLE")
      .pdfData(HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d"))
      .build();
  public static final String TYPE = "TYPE";
  @Mock
  PrintCertificateIntegrationService printCertificateIntegrationService;
  @Mock
  MonitoringLogService monitorLogService;
  @Mock
  GetCertificateService getCertificateService;
  @InjectMocks
  PrintCertificateService printCertificateService;

  @BeforeEach
  void setup() {
    when(printCertificateIntegrationService.print(any(PrintCertificateIntegrationRequest.class)))
        .thenReturn(EXPECTED_RESPONSE);

    when(getCertificateService.get(any(GetCertificateRequest.class)))
        .thenReturn(GetCertificateResponse
            .builder()
            .certificate(
                FormattedCertificate
                    .builder()
                    .metadata(
                        CertificateMetadata
                            .builder()
                            .type(
                                CertificateType.builder().id(TYPE).build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
        );
  }

  @Test
  void shouldSendCertificateIdInRequest() {
    final var captor = ArgumentCaptor.forClass(PrintCertificateIntegrationRequest.class);

    printCertificateService.print(REQUEST);

    verify(printCertificateIntegrationService).print(captor.capture());
    assertEquals(REQUEST.getCertificateId(), captor.getValue().getCertificateId());
  }

  @Test
  void shouldSendCertificateIdInRequestToGetCertificate() {
    final var captor = ArgumentCaptor.forClass(GetCertificateRequest.class);

    printCertificateService.print(REQUEST);

    verify(getCertificateService).get(captor.capture());
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
  void shouldPerformMonitorLoggingWithCertificateId() {
    final var captor = ArgumentCaptor.forClass(String.class);

    printCertificateService.print(REQUEST);

    verify(monitorLogService, times(1)).logCertificatePrinted(
        captor.capture(),
        anyString(),
        anyBoolean()
    );

    assertEquals(REQUEST.getCertificateId(), captor.getValue());
  }

  @Test
  void shouldPerformMonitorLoggingWithCertificateType() {
    final var captor = ArgumentCaptor.forClass(String.class);

    printCertificateService.print(REQUEST);

    verify(monitorLogService, times(1)).logCertificatePrinted(
        anyString(),
        captor.capture(),
        anyBoolean()
    );

    assertEquals(TYPE, captor.getValue());
  }

  @Test
  void shouldPerformMonitorLoggingWithIsEmployerCopy() {
    final var captor = ArgumentCaptor.forClass(Boolean.class);

    printCertificateService.print(
        PrintCertificateRequest
            .builder()
            .customizationId("hideDiagnosis")
            .certificateId("ID")
            .build()
    );

    verify(monitorLogService, times(1)).logCertificatePrinted(
        anyString(),
        anyString(),
        captor.capture()
    );

    assertTrue(captor.getValue());
  }

  @Test
  void shouldPerformMonitorLoggingWithIsNotEmployerCopy() {
    final var captor = ArgumentCaptor.forClass(Boolean.class);

    printCertificateService.print(
        PrintCertificateRequest
            .builder()
            .customizationId("showDiagnosis")
            .certificateId("ID")
            .build()
    );

    verify(monitorLogService, times(1)).logCertificatePrinted(
        anyString(),
        anyString(),
        captor.capture()
    );

    assertFalse(captor.getValue());
  }
}