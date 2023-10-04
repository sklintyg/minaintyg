package se.inera.intyg.minaintyg.integration.webcert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateType;
import se.inera.intyg.minaintyg.integration.webcert.client.GetCertificateFromWebcertService;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateResponseDTO;
import se.inera.intyg.minaintyg.integration.webcert.converter.data.CertificateCategoryConverter;

@ExtendWith(MockitoExtension.class)
class WebcertCertificateIntegrationServiceTest {

  private static final String ID = "id";
  private static final String NAME = "name";
  private static final String CERTIFICATE_ID = "certificateId";
  @Mock
  private GetCertificateFromWebcertService getCertificateFromWebcertService;

  @Mock
  private CertificateCategoryConverter certificateCategoryConverter;

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

  @Test
  void shouldReturnGetCertificateIntegrationResponseWithCategories() {
    final var request = GetCertificateIntegrationRequest.builder()
        .certificateId(CERTIFICATE_ID)
        .build();
    final var response = CertificateResponseDTO.builder()
        .certificate(
            CertificateDTO.builder()
                .data(
                    Map.of(ID, CertificateDataElement.builder().build())
                )
                .metadata(
                    CertificateMetadataDTO.builder()
                        .id(ID)
                        .name(NAME)
                        .build()
                )
                .build()
        )
        .build();
    final var expectedResult = List.of(
        CertificateCategory.builder().build()
    );
    when(getCertificateFromWebcertService.get(request)).thenReturn(response);
    when(certificateCategoryConverter.convert(response.getCertificate())).thenReturn(
        expectedResult);
    final var result = webcertCertificateIntegrationService.get(request);
    assertEquals(expectedResult, result.getCertificate().getCategories());
  }

  @Test
  void shouldReturnGetCertificateIntegrationResponseWithMetaData() {
    final var request = GetCertificateIntegrationRequest.builder()
        .certificateId(CERTIFICATE_ID)
        .build();
    final var response = CertificateResponseDTO.builder()
        .certificate(
            CertificateDTO.builder()
                .data(
                    Map.of(ID, CertificateDataElement.builder().build())
                )
                .metadata(
                    CertificateMetadataDTO.builder()
                        .id(ID)
                        .name(NAME)
                        .build()
                )
                .build()
        )
        .build();
    when(getCertificateFromWebcertService.get(request)).thenReturn(response);
    when(certificateCategoryConverter.convert(response.getCertificate())).thenReturn(
        List.of(CertificateCategory.builder().build()
        )
    );
    final var expectedMetadata = CertificateMetadata.builder()
        .type(
            CertificateType.builder()
                .id(ID)
                .name(NAME)
                .build()
        )
        .build();
    final var result = webcertCertificateIntegrationService.get(request);
    assertEquals(expectedMetadata, result.getCertificate().getMetadata());
  }
}
