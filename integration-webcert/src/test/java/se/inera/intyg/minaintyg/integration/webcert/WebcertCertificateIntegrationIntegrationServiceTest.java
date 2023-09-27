package se.inera.intyg.minaintyg.integration.webcert;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.webcert.client.GetCertificateFromWebcertService;

@ExtendWith(MockitoExtension.class)
class WebcertCertificateIntegrationIntegrationServiceTest {

  @Mock
  GetCertificateFromWebcertService getCertificateFromWebcertService;

  @InjectMocks
  WebcertCertificateIntegrationIntegrationService webcertCertificateIntegrationIntegrationService;

  @Nested
  class ErrorHandling {

    @Test
    void shouldThrowIllegalArgumentExceptionIfRequestIsNull() {
      assertThrows(IllegalArgumentException.class,
          () -> webcertCertificateIntegrationIntegrationService.get(null));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfRequestContainsNullPatientId() {
      final var request = GetCertificateIntegrationRequest.builder().certificateId(null).build();
      assertThrows(IllegalArgumentException.class,
          () -> webcertCertificateIntegrationIntegrationService.get(request));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfRequestContainsEmptyPatientId() {
      final var request = GetCertificateIntegrationRequest.builder().certificateId("").build();
      assertThrows(IllegalArgumentException.class,
          () -> webcertCertificateIntegrationIntegrationService.get(request));
    }

    @Test
    void shouldReturnStatusErrorIfCommunicationErrorWithIntygProxyOccurs() {
      final var request = GetCertificateIntegrationRequest.builder().certificateId("ID")
          .build();
      when(getCertificateFromWebcertService.get(request)).thenThrow(
          RuntimeException.class);
      assertThrows(RuntimeException.class,
          () -> webcertCertificateIntegrationIntegrationService.get(request));
    }
  }

}