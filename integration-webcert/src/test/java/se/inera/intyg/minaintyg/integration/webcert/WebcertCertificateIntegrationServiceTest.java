package se.inera.intyg.minaintyg.integration.webcert;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.Collections;
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
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateText;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.AvailableFunction;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateType;
import se.inera.intyg.minaintyg.integration.common.IllegalCertificateAccessException;
import se.inera.intyg.minaintyg.integration.webcert.client.GetCertificateFromWebcertService;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.AvailableFunctionDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateResponseDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateTextDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.Patient;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.metadata.PersonId;
import se.inera.intyg.minaintyg.integration.webcert.converter.availablefunction.AvailableFunctionConverter;
import se.inera.intyg.minaintyg.integration.webcert.converter.data.CertificateDataConverter;
import se.inera.intyg.minaintyg.integration.webcert.converter.metadata.MetadataConverter;
import se.inera.intyg.minaintyg.integration.webcert.converter.text.CertificateTextConverter;

@ExtendWith(MockitoExtension.class)
class WebcertCertificateIntegrationServiceTest {

  private static final String ID = "id";
  private static final String NAME = "name";
  private static final String CERTIFICATE_ID = "certificateId";
  private static final String PERSON_ID = "personId";
  private static final String PERSON_ID_DASH = "person-Id";
  private static final Patient PATIENT = Patient.builder().personId(
      PersonId.builder().id(PERSON_ID).build()).build();
  public static final GetCertificateIntegrationRequest REQUEST = GetCertificateIntegrationRequest.builder()
      .certificateId(CERTIFICATE_ID)
      .personId(PERSON_ID)
      .build();

  @Mock
  private GetCertificateFromWebcertService getCertificateFromWebcertService;

  @Mock
  private CertificateDataConverter certificateDataConverter;

  @Mock
  private MetadataConverter metadataConverter;

  @Mock
  private AvailableFunctionConverter availableFunctionConverter;

  @Mock
  private CertificateTextConverter certificateTextConverter;

  @InjectMocks
  private WebcertCertificateIntegrationService webcertCertificateIntegrationService;

  @Test
  void shouldThrowIfRequestIsNull() {
    assertThrows(IllegalArgumentException.class,
        () -> webcertCertificateIntegrationService.get(null));
  }

  @Test
  void shouldThrowIfCertificateIdFromRequestIsNull() {
    final var invalidRequest = GetCertificateIntegrationRequest.builder().build();
    assertThrows(IllegalArgumentException.class,
        () -> webcertCertificateIntegrationService.get(invalidRequest));
  }


  @Test
  void shouldThrowIfCertificateIdFromRequestIsEmpty() {
    final var invalidRequest = GetCertificateIntegrationRequest.builder()
        .certificateId("")
        .build();

    assertThrows(IllegalArgumentException.class,
        () -> webcertCertificateIntegrationService.get(invalidRequest));
  }

  @Test
  void shouldThrowIfNoCertificateWasFound() {
    when(getCertificateFromWebcertService.get(REQUEST)).thenReturn(
        CertificateResponseDTO.builder()
            .certificate(null)
            .build()
    );
    assertThrows(RuntimeException.class, () -> webcertCertificateIntegrationService.get(REQUEST));
  }

  @Test
  void shouldNotThrowErrorIfPatientIdIsNotMatchWithDash() {
    final var request = GetCertificateIntegrationRequest.builder()
        .certificateId(CERTIFICATE_ID)
        .personId(PERSON_ID_DASH)
        .build();

    when(getCertificateFromWebcertService.get(request)).thenReturn(
        CertificateResponseDTO.builder()
            .certificate(CertificateDTO.builder()
                .data(
                    Map.of(ID, CertificateDataElement.builder().build())
                )
                .metadata(
                    CertificateMetadataDTO.builder()
                        .patient(PATIENT)
                        .build()
                )
                .build())
            .build()
    );
    assertDoesNotThrow(() -> webcertCertificateIntegrationService.get(request));
  }

  @Test
  void shouldThrowErrorIfPatientIdIsNotMatchForCertificatePatientId() {
    final var request = GetCertificateIntegrationRequest.builder()
        .certificateId(CERTIFICATE_ID)
        .personId("NO_MATCH")
        .build();

    when(getCertificateFromWebcertService.get(request)).thenReturn(
        CertificateResponseDTO.builder()
            .certificate(CertificateDTO.builder()
                .data(
                    Map.of(ID, CertificateDataElement.builder().build())
                )
                .metadata(
                    CertificateMetadataDTO.builder()
                        .patient(PATIENT)
                        .build()
                )
                .build())
            .build()
    );
    assertThrows(IllegalCertificateAccessException.class,
        () -> webcertCertificateIntegrationService.get(request));
  }

  @Test
  void shouldReturnGetCertificateIntegrationResponseWithCategories() {
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
                        .patient(PATIENT)
                        .build()
                )
                .build()
        )
        .texts(Collections.emptyList())
        .build();
    final var expectedResult = List.of(
        CertificateCategory.builder().build()
    );
    when(getCertificateFromWebcertService.get(REQUEST)).thenReturn(response);
    when(certificateDataConverter.convert(anyList())).thenReturn(
        expectedResult);
    when(metadataConverter.convert(any(CertificateMetadataDTO.class))).thenReturn(
        CertificateMetadata.builder()
            .build());

    final var result = webcertCertificateIntegrationService.get(REQUEST);
    assertEquals(expectedResult, result.getCertificate().getCategories());
  }

  @Test
  void shouldReturnGetCertificateIntegrationResponseWithMetaData() {
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
                        .patient(PATIENT)
                        .build()
                )
                .build()
        )
        .texts(Collections.emptyList())
        .build();

    final var expectedMetadata = CertificateMetadata.builder()
        .type(
            CertificateType.builder()
                .id(ID)
                .name(NAME)
                .build()
        )
        .build();

    when(getCertificateFromWebcertService.get(REQUEST)).thenReturn(response);
    when(certificateDataConverter.convert(anyList())).thenReturn(
        List.of(CertificateCategory.builder().build()
        )
    );
    when(metadataConverter.convert(any(CertificateMetadataDTO.class))).thenReturn(
        expectedMetadata);

    final var result = webcertCertificateIntegrationService.get(REQUEST);
    assertEquals(expectedMetadata, result.getCertificate().getMetadata());
  }

  @Test
  void shouldReturnResponseWithAvailableFunctions() {
    final var expectedAvailableFunctions = List.of(
        AvailableFunction.builder().build(),
        AvailableFunction.builder().build()
    );

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
                        .patient(PATIENT)
                        .build()
                )
                .build()
        )
        .availableFunctions(
            List.of(
                AvailableFunctionDTO.builder().build(),
                AvailableFunctionDTO.builder().build()
            )
        )
        .texts(Collections.emptyList())
        .build();

    when(getCertificateFromWebcertService.get(REQUEST)).thenReturn(response);
    when(availableFunctionConverter.convert(response.getAvailableFunctions()))
        .thenReturn(expectedAvailableFunctions);

    final var result = webcertCertificateIntegrationService.get(REQUEST);

    assertEquals(expectedAvailableFunctions, result.getAvailableFunctions());
  }

  @Test
  void shouldReturnResponseWithTexts() {
    final var expectedCertificateText = CertificateText.builder().build();

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
                        .patient(PATIENT)
                        .build()
                )
                .build()
        )
        .texts(
            List.of(
                CertificateTextDTO.builder().build(),
                CertificateTextDTO.builder().build()
            )
        )
        .build();

    when(getCertificateFromWebcertService.get(REQUEST)).thenReturn(response);
    when(certificateTextConverter.convert(any(CertificateTextDTO.class)))
        .thenReturn(expectedCertificateText);

    final var result = webcertCertificateIntegrationService.get(REQUEST);

    assertEquals(2, result.getTexts().size());
    assertEquals(expectedCertificateText, result.getTexts().get(0));
    assertEquals(expectedCertificateText, result.getTexts().get(1));
  }

  @Test
  void shouldReturnResponseWithNoTexts() {
    final var expectedCertificateText = Collections.emptyList();

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
                        .patient(PATIENT)
                        .build()
                )
                .build()
        )
        .build();

    when(getCertificateFromWebcertService.get(REQUEST)).thenReturn(response);

    final var result = webcertCertificateIntegrationService.get(REQUEST);

    assertEquals(expectedCertificateText, result.getTexts());
  }
}
