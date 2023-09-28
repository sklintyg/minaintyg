package se.inera.intyg.minaintyg.integration.webcert;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.webcert.client.GetCertificateFromWebcertService;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateResponseDTO;

@ExtendWith(MockitoExtension.class)
class WebcertCertificateIntegrationServiceTest {

  private static final String CERTIFICATE_ID = "certificateId";
  @Mock
  private GetCertificateFromWebcertService getCertificateFromWebcertService;

  @Mock
  private ConvertCertificateService convertCertificateService;

  @Mock
  private CategoryQuestionOrganizer categoryQuestionOrganizer;

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

  @Test
  void shouldThrowIfNoCertificateWasFound() {
    final var request = GetCertificateIntegrationRequest.builder()
        .certificateId(CERTIFICATE_ID)
        .build();
    when(getCertificateFromWebcertService.get(request)).thenReturn(
        CertificateResponseDTO.builder()
            .certificate(null)
            .build()
    );
    assertThrows(RuntimeException.class, () -> webcertCertificateIntegrationService.get(request));
  }
}
