package se.inera.intyg.minaintyg.integration.webcert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HexFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.PrintCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.webcert.client.PrintCertificateFromWebcertService;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.PrintCertificateResponseDTO;

@ExtendWith(MockitoExtension.class)
class WebcertPrintCertificateIntegrationServiceTest {

    private static final String FILENAME = "NAME";
    private static final byte[] PDF_DATA = HexFormat.of()
        .parseHex("e04fd020ea3a6910a2d808002b30309d");
    private static final PrintCertificateResponseDTO EXPECTED_RESPONSE = PrintCertificateResponseDTO
        .builder()
        .pdfData(PDF_DATA)
        .filename(FILENAME)
        .build();
    private static final PrintCertificateIntegrationRequest REQUEST = PrintCertificateIntegrationRequest
        .builder()
        .certificateId("ID")
        .customizationId("C_ID")
        .build();

    @Mock
    PrintCertificateFromWebcertService printCertificateFromWebcertService;
    @InjectMocks
    WebcertPrintCertificateIntegrationService webcertPrintCertificateIntegrationService;

    @Nested
    class ConvertedResponse {

        @BeforeEach
        void setup() {
            when(printCertificateFromWebcertService.print(
                any(PrintCertificateIntegrationRequest.class)))
                .thenReturn(EXPECTED_RESPONSE);
        }

        @Test
        void shouldReturnFilenameFromIntegrationService() {
            final var response = webcertPrintCertificateIntegrationService.print(REQUEST);

            assertEquals(EXPECTED_RESPONSE.getFilename(), response.getFilename());
        }

        @Test
        void shouldReturnPdfDataFromIntegrationService() {
            final var response = webcertPrintCertificateIntegrationService.print(REQUEST);

            assertEquals(EXPECTED_RESPONSE.getPdfData(), response.getPdfData());
        }
    }

    @Nested
    class ValidateRequest {

        @Test
        void shouldThrowErrorIfRequestIsNull() {
            assertThrows(IllegalArgumentException.class,
                () -> webcertPrintCertificateIntegrationService.print(null)
            );
        }

        @Test
        void shouldThrowErrorIfCertificateIdIsNull() {
            final var request = PrintCertificateIntegrationRequest.builder()
                .customizationId("id")
                .build();

            assertThrows(IllegalArgumentException.class,
                () -> webcertPrintCertificateIntegrationService.print(request)
            );
        }

        @Test
        void shouldThrowErrorIfCertificateIdIsEmpty() {
            final var request = PrintCertificateIntegrationRequest.builder()
                .certificateId("")
                .customizationId("id")
                .build();

            assertThrows(IllegalArgumentException.class,
                () -> webcertPrintCertificateIntegrationService.print(request)
            );
        }
    }

    @Nested
    class ValidateResponse {

        @Test
        void shouldThrowErrorIfResponseIsNull() {
            assertThrows(IllegalArgumentException.class,
                () -> webcertPrintCertificateIntegrationService.print(REQUEST)
            );
        }

        @Test
        void shouldThrowErrorIfFilenameIsNull() {
            when(printCertificateFromWebcertService.print(any(PrintCertificateIntegrationRequest.class)))
                .thenReturn(
                    PrintCertificateResponseDTO
                        .builder()
                        .pdfData(PDF_DATA)
                        .build()
                );

            assertThrows(IllegalArgumentException.class,
                () -> webcertPrintCertificateIntegrationService.print(REQUEST)
            );
        }

        @Test
        void shouldThrowErrorIfFilenameIsEmpty() {
            when(printCertificateFromWebcertService.print(any(PrintCertificateIntegrationRequest.class)))
                .thenReturn(PrintCertificateResponseDTO
                    .builder()
                    .filename("")
                    .pdfData(PDF_DATA)
                    .build()
                );

            assertThrows(IllegalArgumentException.class,
                () -> webcertPrintCertificateIntegrationService.print(REQUEST)
            );
        }

        @Test
        void shouldThrowErrorIfPdfDataIsNull() {
            when(printCertificateFromWebcertService.print(any(PrintCertificateIntegrationRequest.class)))
                .thenReturn(
                    PrintCertificateResponseDTO
                        .builder()
                        .filename(FILENAME)
                        .build()
                );

            assertThrows(IllegalArgumentException.class,
                () -> webcertPrintCertificateIntegrationService.print(REQUEST)
            );
        }

        @Test
        void shouldThrowErrorIfPdfDataIsEmpty() {
            final var empty = HexFormat.of().parseHex("");
            when(printCertificateFromWebcertService.print(any(PrintCertificateIntegrationRequest.class)))
                .thenReturn(
                    PrintCertificateResponseDTO
                        .builder()
                        .filename(FILENAME)
                        .pdfData(empty).build()
                );

            assertThrows(IllegalArgumentException.class,
                () -> webcertPrintCertificateIntegrationService.print(REQUEST)
            );
        }
    }
}