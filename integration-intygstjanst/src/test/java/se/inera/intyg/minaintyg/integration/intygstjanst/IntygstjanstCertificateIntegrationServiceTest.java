package se.inera.intyg.minaintyg.integration.intygstjanst;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificatesRequest;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.GetCertificatesFromIntygstjanstService;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificateDTO;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.dto.CertificatesResponseDTO;

@ExtendWith(MockitoExtension.class)
class IntygstjanstCertificateIntegrationServiceTest {

  private static final String PERSON_ID = "191212121212";
  @Mock
  private GetCertificatesFromIntygstjanstService getCertificatesFromIntygstjanstService;
  @Mock
  private CertificateConverter certificateConverter;
  @InjectMocks
  private IntygstjanstCertificateIntegrationService intygstjanstCertificateIntegrationService;

  @Nested
  class TestResponse {

    CertificatesRequest request;
    CertificatesResponseDTO certificatesResponseDTO;
    CertificateDTO originalCertificate = new CertificateDTO();
    Certificate convertedCertificate;

    @BeforeEach
    void setup() {
      request = CertificatesRequest.builder().patientId(PERSON_ID).build();

      certificatesResponseDTO = CertificatesResponseDTO
          .builder()
          .content(List.of(originalCertificate))
          .build();

      when(getCertificatesFromIntygstjanstService.get(request)).thenReturn(
          certificatesResponseDTO);

      when(certificateConverter.convert(any(CertificateDTO.class))).thenReturn(
          convertedCertificate);
    }

    @Test
    void shouldCallConverter() {
      intygstjanstCertificateIntegrationService.get(request);

      final var captor = ArgumentCaptor.forClass(CertificateDTO.class);

      verify(certificateConverter).convert(captor.capture());

      assertEquals(originalCertificate, captor.getValue());
    }

    @Test
    void shouldReturnConvertedCertificate() {
      final var result = intygstjanstCertificateIntegrationService.get(request);

      assertEquals(convertedCertificate, result.getContent().get(0));
    }
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
