package se.inera.intyg.minaintyg.integration.webcert;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.webcert.client.GetCertificateFromWebcertService;

@ExtendWith(MockitoExtension.class)
class WebcertCertificateIntegrationServiceTest {

  @Mock
  private GetCertificateFromWebcertService getCertificateFromWebcertService;

  @Mock
  private ConvertCertificateService convertCertificateService;

  @InjectMocks
  private WebcertCertificateIntegrationService webcertCertificateIntegrationService;

  @Test
  void shouldThrowIfRequestIsNull() {
    assertThrows(IllegalArgumentException.class,
        () -> webcertCertificateIntegrationService.get(null));
  }

  @Test
  void shouldThrowIfCertificateIdFromRequestIsNull() {
    assertThrows(IllegalArgumentException.class,
        () -> webcertCertificateIntegrationService.get(GetCertificateIntegrationRequest.builder()
            .build()));
  }


  @Test
  void shouldThrowIfCertificateIdFromRequestIsEmpty() {
    assertThrows(IllegalArgumentException.class,
        () -> webcertCertificateIntegrationService.get(GetCertificateIntegrationRequest.builder()
            .certificateId("")
            .build()));
  }
}