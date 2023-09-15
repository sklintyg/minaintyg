package se.inera.intyg.minaintyg.integration.intygstjanst;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificatesRequest;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.GetCertificatesFromIntygstjanstServiceImpl;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificatesResponseDTO;

@ExtendWith(MockitoExtension.class)
class IntygstjanstCertificateIntegrationServiceTest {

  private static final String PERSON_ID = "191212121212";
  @Mock
  private GetCertificatesFromIntygstjanstServiceImpl getCertificatesFromIntygstjanstService;
  @InjectMocks
  private IntygstjanstCertificateIntegrationService intygstjanstCertificateIntegrationService;

  @Test
  void shouldReturnResponse() {
    final var request = CertificatesRequest.builder().patientId(PERSON_ID).build();
    final var expectedResult = CertificatesResponseDTO.builder().build();
    when(getCertificatesFromIntygstjanstService.get(request)).thenReturn(
        expectedResult);
    final var actualResult = intygstjanstCertificateIntegrationService.get(request);
    assertEquals(expectedResult, actualResult);
  }

  @Nested
  class ErrorHandling {

    @Test
    void shouldThrowIlligalArgumentExceptionIfRequestIsNull() {
      assertThrows(IllegalArgumentException.class,
          () -> intygstjanstCertificateIntegrationService.get(null));
    }

    @Test
    void shouldThrowIlligalArgumentExceptionIfRequestContainsNullPatientId() {
      final var request = CertificatesRequest.builder().patientId(null).build();
      assertThrows(IllegalArgumentException.class,
          () -> intygstjanstCertificateIntegrationService.get(request));
    }

    @Test
    void shouldThrowIlligalArgumentExceptionIfRequestContainsEmptyPatientId() {
      final var request = CertificatesRequest.builder().patientId("").build();
      assertThrows(IllegalArgumentException.class,
          () -> intygstjanstCertificateIntegrationService.get(request));
    }

    @Test
    void shouldReturnStatusErrorIfCommunicationErrorWithIntygProxyOccurs() {
      final var request = CertificatesRequest.builder().patientId(PERSON_ID).build();
      when(getCertificatesFromIntygstjanstService.get(request)).thenThrow(
          RuntimeException.class);
      assertThrows(RuntimeException.class,
          () -> intygstjanstCertificateIntegrationService.get(request));
    }
  }
}
