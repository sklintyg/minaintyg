package se.inera.intyg.minaintyg.certificate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import se.inera.intyg.minaintyg.auth.MinaIntygUser;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;
import se.inera.intyg.minaintyg.user.UserService;

@ExtendWith(MockitoExtension.class)
class GetCertificateServiceTest {

  private static final String ID = "ID";
  private static final String PATIENT_ID = "PATIENT_ID";
  private static final GetCertificateRequest REQUEST = GetCertificateRequest
      .builder()
      .certificateId(ID)
      .build();

  private static final GetCertificateIntegrationResponse EXPECTED_RESPONSE = GetCertificateIntegrationResponse
      .builder()
      .certificate(
          Certificate
              .builder()
              .metadata(CertificateMetadata.builder().build())
              .categories(List.of(CertificateCategory.builder().build()))
              .build()
      )
      .build();

  @Mock
  MonitoringLogService monitoringLogService;
  @Mock
  UserService userService;
  @Mock
  FormattedCertificateConverter formattedCertificateConverter;
  @Mock
  GetCertificateIntegrationService getCertificateIntegrationService;
  @InjectMocks
  GetCertificateService getCertificateService;

  @BeforeEach
  void setup() {
    when(userService.getLoggedInUser())
        .thenReturn(Optional.ofNullable(MinaIntygUser.builder().personId(PATIENT_ID).build()));

    when(getCertificateIntegrationService.get(any())).thenReturn(EXPECTED_RESPONSE);
  }

  @Nested
  class MonitorLogService {

    @Test
    void shouldLogWithCertificateId() {
      final var captor = ArgumentCaptor.forClass(String.class);

      getCertificateService.get(REQUEST);

      verify(monitoringLogService).logGetCertificate(anyString(), captor.capture());
      assertEquals(ID, captor.getValue());
    }

    @Test
    void shouldLogWithPatientIdId() {
      final var captor = ArgumentCaptor.forClass(String.class);

      getCertificateService.get(REQUEST);

      verify(monitoringLogService).logGetCertificate(captor.capture(), anyString());
      assertEquals(PATIENT_ID, captor.getValue());
    }
  }

  @Nested
  class GetCompleteCertificate {

    @Test
    void shouldSetCertificateId() {
      final var captor = ArgumentCaptor.forClass(GetCertificateIntegrationRequest.class);

      getCertificateService.get(REQUEST);

      verify(getCertificateIntegrationService).get(captor.capture());
      assertEquals(ID, captor.getValue().getCertificateId());
    }
  }

  @Nested
  class Converter {

    @Test
    void shouldSendCertificateToConverter() {
      final var captor = ArgumentCaptor.forClass(Certificate.class);

      getCertificateService.get(REQUEST);

      verify(formattedCertificateConverter).convert(captor.capture());
      assertEquals(EXPECTED_RESPONSE.getCertificate(), captor.getValue());
    }
  }

}