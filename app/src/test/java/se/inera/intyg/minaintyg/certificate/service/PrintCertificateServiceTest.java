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
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.auth.LoginMethod;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;
import se.inera.intyg.minaintyg.certificate.service.dto.PrintCertificateRequest;
import se.inera.intyg.minaintyg.integration.api.analytics.AnalyticsMessageFactory;
import se.inera.intyg.minaintyg.integration.api.analytics.PublishAnalyticsMessage;
import se.inera.intyg.minaintyg.integration.api.analytics.model.CertificateAnalyticsMessage;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateType;
import se.inera.intyg.minaintyg.logging.service.MonitoringLogService;
import se.inera.intyg.minaintyg.user.UserService;

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
  private static final String PERSON_ID = "personId";

  @Mock
  PrintCertificateIntegrationService printCertificateIntegrationService;
  @Mock
  MonitoringLogService monitorLogService;
  @Mock
  GetCertificateIntegrationService getCertificateIntegrationService;
  @Mock
  UserService userService;
  @Mock
  AnalyticsMessageFactory analyticsMessageFactory;
  @Mock
  PublishAnalyticsMessage publishAnalyticsMessage;
  @InjectMocks
  PrintCertificateService printCertificateService;

  private Certificate certificate;

  @BeforeEach
  void setup() {
    when(printCertificateIntegrationService.print(any(PrintCertificateIntegrationRequest.class)))
        .thenReturn(EXPECTED_RESPONSE);

    certificate = Certificate
        .builder()
        .metadata(
            CertificateMetadata
                .builder()
                .type(
                    CertificateType.builder().id(TYPE).build()
                )
                .build()
        )
        .build();

    when(getCertificateIntegrationService.get(any(GetCertificateIntegrationRequest.class)))
        .thenReturn(
            GetCertificateIntegrationResponse
                .builder()
                .certificate(
                    certificate
                )
                .build()
        );
    when(userService.getLoggedInUser()).thenReturn(
        Optional.of(new MinaIntygUser(PERSON_ID, "personName", LoginMethod.ELVA77)));
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
    final var captor = ArgumentCaptor.forClass(GetCertificateIntegrationRequest.class);

    printCertificateService.print(REQUEST);

    verify(getCertificateIntegrationService).get(captor.capture());
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
  void shouldSendPersonIdInRequest() {
    final var captor = ArgumentCaptor.forClass(PrintCertificateIntegrationRequest.class);

    printCertificateService.print(REQUEST);

    verify(printCertificateIntegrationService).print(captor.capture());
    assertEquals(PERSON_ID, captor.getValue().getPersonId());
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
  void shouldPerformMonitorLoggingWithIsNotEmployerCopyIfCustomizationIdIsNull() {
    final var captor = ArgumentCaptor.forClass(Boolean.class);

    printCertificateService.print(
        PrintCertificateRequest
            .builder()
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

  @Test
  void shouldPerformMonitorLoggingWithIsNotEmployerCopyIfCustomizationIdIsEmpty() {
    final var captor = ArgumentCaptor.forClass(Boolean.class);

    printCertificateService.print(
        PrintCertificateRequest
            .builder()
            .certificateId("ID")
            .customizationId("")
            .build()
    );

    verify(monitorLogService, times(1)).logCertificatePrinted(
        anyString(),
        anyString(),
        captor.capture()
    );

    assertFalse(captor.getValue());
  }

  @Test
  void shouldPublishAnalyticsMessageWhenCertificateIsPrinted() {
    final var analyticsMessage = CertificateAnalyticsMessage.builder().build();
    when(analyticsMessageFactory.certificatePrinted(certificate)).thenReturn(analyticsMessage);

    printCertificateService.print(
        PrintCertificateRequest
            .builder()
            .certificateId("ID")
            .customizationId("")
            .build()
    );

    verify(publishAnalyticsMessage, times(1)).publishEvent(analyticsMessage);
  }

  @Test
  void shouldPublishAnalyticsMessageWhenCertificateIsPrintedCustomized() {
    final var analyticsMessage = CertificateAnalyticsMessage.builder().build();
    when(analyticsMessageFactory.certificatePrintedCustomized(certificate))
        .thenReturn(analyticsMessage);

    printCertificateService.print(
        PrintCertificateRequest
            .builder()
            .certificateId("ID")
            .customizationId("ID")
            .build()
    );

    verify(publishAnalyticsMessage, times(1)).publishEvent(analyticsMessage);
  }
}