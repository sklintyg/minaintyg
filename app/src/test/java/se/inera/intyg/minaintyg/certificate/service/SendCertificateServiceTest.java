package se.inera.intyg.minaintyg.certificate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;
import se.inera.intyg.minaintyg.certificate.service.dto.SendCertificateRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.SendCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.SendCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateRecipient;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateType;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;
import se.inera.intyg.minaintyg.user.UserService;

@ExtendWith(MockitoExtension.class)
class SendCertificateServiceTest {

  private static final String CERTIFICATE_ID = "CERTIFICATE_ID";
  private static final String PATIENT_ID = "PATIENT_ID";
  private static final String RECIPIENT_ID = "R_ID";
  private static final String RECIPIENT_NAME = "R_NAME";
  private static final String TYPE_ID = "T_ID";
  private static final String TYPE_NAME = "T_NAME";
  private static final SendCertificateRequest REQUEST = SendCertificateRequest
      .builder()
      .certificateId(CERTIFICATE_ID)
      .build();

  private static final Certificate CERTIFICATE_SENT = getCertificate(
      CertificateRecipient
          .builder()
          .id(RECIPIENT_ID)
          .name(RECIPIENT_NAME)
          .sent(LocalDateTime.now())
          .build()
  );

  private static final Certificate CERTIFICATE_NOT_SENT = getCertificate(
      CertificateRecipient
          .builder()
          .id(RECIPIENT_ID)
          .name(RECIPIENT_NAME)
          .build()
  );

  private static final Certificate CERTIFICATE_NO_RECIPIENT = getCertificate(null);
  
  @Mock
  SendCertificateIntegrationService sendCertificateIntegrationService;
  @Mock
  MonitoringLogService monitoringLogService;
  @Mock
  UserService userService;
  @Mock
  GetCertificateIntegrationService getCertificateIntegrationService;
  @InjectMocks
  SendCertificateService sendCertificateService;

  private static Certificate getCertificate(CertificateRecipient certificateRecipient) {
    return Certificate
        .builder()
        .metadata(
            CertificateMetadata
                .builder()
                .recipient(certificateRecipient)
                .type(
                    CertificateType
                        .builder()
                        .id(TYPE_ID)
                        .name(TYPE_NAME)
                        .build()
                )
                .build()
        )
        .build();
  }

  @BeforeEach
  void setup() {
    when(userService.getLoggedInUser()).thenReturn(Optional.of(MinaIntygUser
        .builder()
        .personId(PATIENT_ID)
        .build()));
  }

  @Nested
  class HasRecipient {

    @BeforeEach
    void setup() {
      when(getCertificateIntegrationService.get(any(GetCertificateIntegrationRequest.class)))
          .thenReturn(
              GetCertificateIntegrationResponse.builder().certificate(CERTIFICATE_NOT_SENT).build()
          );
    }

    @Nested
    class RequestToIntygstjanst {

      @Test
      void shouldSendCertificateId() {
        sendCertificateService.send(REQUEST);

        final var captor = ArgumentCaptor.forClass(SendCertificateIntegrationRequest.class);

        verify(sendCertificateIntegrationService).send(captor.capture());
        assertEquals(CERTIFICATE_ID, captor.getValue().getCertificateId());
      }

      @Test
      void shouldSendRecipient() {
        sendCertificateService.send(REQUEST);

        final var captor = ArgumentCaptor.forClass(SendCertificateIntegrationRequest.class);

        verify(sendCertificateIntegrationService).send(captor.capture());
        assertEquals(RECIPIENT_ID, captor.getValue().getRecipient());
      }

      @Test
      void shouldSendPatientId() {
        sendCertificateService.send(REQUEST);

        final var captor = ArgumentCaptor.forClass(SendCertificateIntegrationRequest.class);

        verify(sendCertificateIntegrationService).send(captor.capture());
        assertEquals(PATIENT_ID, captor.getValue().getPatientId());
      }
    }

    @Nested
    class MonitorLogging {

      @Test
      void shouldSendCertificateId() {
        sendCertificateService.send(REQUEST);

        final var captor = ArgumentCaptor.forClass(String.class);

        verify(monitoringLogService).logCertificateSent(captor.capture(), anyString(), anyString());
        assertEquals(CERTIFICATE_ID, captor.getValue());
      }

      @Test
      void shouldSendCertificateType() {
        sendCertificateService.send(REQUEST);

        final var captor = ArgumentCaptor.forClass(String.class);

        verify(monitoringLogService).logCertificateSent(anyString(), captor.capture(), anyString());
        assertEquals(TYPE_ID, captor.getValue());
      }

      @Test
      void shouldSendRecipient() {
        sendCertificateService.send(REQUEST);

        final var captor = ArgumentCaptor.forClass(String.class);

        verify(monitoringLogService).logCertificateSent(anyString(), anyString(), captor.capture());
        assertEquals(RECIPIENT_ID, captor.getValue());
      }
    }
  }

  @Nested
  class ActionValidation {

    @Nested
    class AlreadySent {

      @BeforeEach
      void setup() {
        when(getCertificateIntegrationService.get(any(GetCertificateIntegrationRequest.class)))
            .thenReturn(
                GetCertificateIntegrationResponse.builder().certificate(CERTIFICATE_SENT).build()
            );
      }

      @Test
      void shouldThrowException() {
        assertThrows(IllegalStateException.class, () -> sendCertificateService.send(REQUEST));
      }
    }

    @Nested
    class NoRecipient {

      @BeforeEach
      void setup() {
        when(getCertificateIntegrationService.get(any(GetCertificateIntegrationRequest.class)))
            .thenReturn(
                GetCertificateIntegrationResponse.builder().certificate(CERTIFICATE_NO_RECIPIENT)
                    .build()
            );
      }

      @Test
      void shouldThrowException() {
        assertThrows(IllegalStateException.class, () -> sendCertificateService.send(REQUEST));
      }
    }
  }

}