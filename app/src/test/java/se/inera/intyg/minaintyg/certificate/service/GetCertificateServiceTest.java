package se.inera.intyg.minaintyg.certificate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import se.inera.intyg.minaintyg.certificate.service.dto.FormattedCertificate;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationService;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.AvailableFunction;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateType;
import se.inera.intyg.minaintyg.logging.MonitoringLogService;

@ExtendWith(MockitoExtension.class)
class GetCertificateServiceTest {

  private static final String ID = "ID";
  private static final String TYPE = "Type";
  private static final GetCertificateRequest REQUEST = GetCertificateRequest
      .builder()
      .certificateId(ID)
      .build();

  private static final GetCertificateIntegrationResponse EXPECTED_RESPONSE = GetCertificateIntegrationResponse
      .builder()
      .certificate(
          Certificate
              .builder()
              .metadata(CertificateMetadata
                  .builder()
                  .type(
                      CertificateType
                          .builder()
                          .id(TYPE)
                          .build()
                  )
                  .build())
              .categories(List.of(CertificateCategory.builder().build()))
              .build()
      )
      .availableFunctions(List.of(AvailableFunction.builder().build()))
      .build();

  @Mock
  MonitoringLogService monitoringLogService;
  @Mock
  FormattedCertificateConverter formattedCertificateConverter;
  @Mock
  GetCertificateIntegrationService getCertificateIntegrationService;
  @InjectMocks
  GetCertificateService getCertificateService;

  @BeforeEach
  void setup() {
    when(getCertificateIntegrationService.get(any())).thenReturn(EXPECTED_RESPONSE);
  }

  @Nested
  class MonitorLogService {

    @Test
    void shouldLogWithCertificateId() {
      final var captor = ArgumentCaptor.forClass(String.class);

      getCertificateService.get(REQUEST);

      verify(monitoringLogService).logCertificateRead(captor.capture(), anyString());
      assertEquals(ID, captor.getValue());
    }

    @Test
    void shouldLogWithCertificateType() {
      final var captor = ArgumentCaptor.forClass(String.class);

      getCertificateService.get(REQUEST);

      verify(monitoringLogService).logCertificateRead(anyString(), captor.capture());
      assertEquals(TYPE, captor.getValue());
    }
  }

  @Nested
  class Request {

    @Test
    void shouldSetCertificateId() {
      final var captor = ArgumentCaptor.forClass(GetCertificateIntegrationRequest.class);

      getCertificateService.get(REQUEST);

      verify(getCertificateIntegrationService).get(captor.capture());
      assertEquals(ID, captor.getValue().getCertificateId());
    }
  }

  @Nested
  class Response {

    @Test
    void shouldSendCertificateToConverter() {
      final var captor = ArgumentCaptor.forClass(Certificate.class);

      getCertificateService.get(REQUEST);

      verify(formattedCertificateConverter).convert(captor.capture());
      assertEquals(EXPECTED_RESPONSE.getCertificate(), captor.getValue());
    }

    @Test
    void shouldSetConvertedCertificate() {
      final var expectedFormattedCertificate = FormattedCertificate.builder().build();
      when(formattedCertificateConverter.convert(any(Certificate.class)))
          .thenReturn(expectedFormattedCertificate);

      final var response = getCertificateService.get(REQUEST);

      assertEquals(response.getCertificate(), expectedFormattedCertificate);
    }

    @Test
    void shouldSetAvailableFunctions() {
      final var response = getCertificateService.get(REQUEST);

      assertEquals(EXPECTED_RESPONSE.getAvailableFunctions(), response.getAvailableFunctions());
    }
  }

}