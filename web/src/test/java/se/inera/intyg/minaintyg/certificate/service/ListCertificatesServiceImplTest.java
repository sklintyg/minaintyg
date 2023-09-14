package se.inera.intyg.minaintyg.certificate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.auth.MinaIntygUser;
import se.inera.intyg.minaintyg.certificate.service.dto.CertificateDTO;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.GetCertificatesService;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificatesRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.dto.CertificatesResponse;
import se.inera.intyg.minaintyg.user.MinaIntygUserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListCertificatesServiceImplTest {

    private static final String PATIENT_ID = "PatientId";
    private static final List<String> YEARS = List.of("2020");
    private static final List<String> UNITS = List.of("unit1");
    private static final List<String> TYPES = List.of("lisjp");
    private static final List<CertificateStatusType> STATUSES = List.of(CertificateStatusType.SENT);
    private static final se.inera.intyg.minaintyg.integration.api.certificate.dto.Certificate originalCertificate =
            se.inera.intyg.minaintyg.integration.api.certificate.dto.Certificate.builder().build();
    private static final CertificateDTO convertedCertificate = CertificateDTO.builder().build();

    @Mock
    CertificateConverter certificateConverter;

    @Mock
    GetCertificatesService getCertificatesService;

    @Mock
    MinaIntygUserService minaIntygUserService;

    @InjectMocks
    ListCertificatesServiceImpl listCertificatesService;

    @BeforeEach
    void setup() {
        when(minaIntygUserService.getUser()).thenReturn(new MinaIntygUser(PATIENT_ID, "name"));

        when(getCertificatesService.get(any())).thenReturn(
                CertificatesResponse
                        .builder()
                        .content(List.of(originalCertificate))
                        .build()
        );

        when(certificateConverter.convert(
                any(se.inera.intyg.minaintyg.integration.api.certificate.dto.Certificate.class))
        ).thenReturn(convertedCertificate);
    }

    @Nested
    class Request {

        ListCertificatesRequest request = ListCertificatesRequest
                .builder()
                .years(YEARS)
                .units(UNITS)
                .certificateTypes(TYPES)
                .statuses(STATUSES)
                .build();

        @Test
        void shouldSendPatientId() {
            listCertificatesService.get(request);

            final var captor = ArgumentCaptor.forClass(CertificatesRequest.class);

            verify(getCertificatesService).get(captor.capture());
            assertEquals(PATIENT_ID, captor.getValue().getPatientId());
        }

        @Test
        void shouldSendYears() {
            listCertificatesService.get(request);

            final var captor = ArgumentCaptor.forClass(CertificatesRequest.class);

            verify(getCertificatesService).get(captor.capture());
            assertEquals(YEARS, captor.getValue().getYears());
        }

        @Test
        void shouldSendUnits() {
            listCertificatesService.get(request);

            final var captor = ArgumentCaptor.forClass(CertificatesRequest.class);

            verify(getCertificatesService).get(captor.capture());
            assertEquals(UNITS, captor.getValue().getUnits());
        }

        @Test
        void shouldSendCertificateTypes() {
            listCertificatesService.get(request);

            final var captor = ArgumentCaptor.forClass(CertificatesRequest.class);

            verify(getCertificatesService).get(captor.capture());
            assertEquals(TYPES, captor.getValue().getCertificateTypes());
        }

        @Test
        void shouldSendStatuses() {
            listCertificatesService.get(request);

            final var captor = ArgumentCaptor.forClass(CertificatesRequest.class);

            verify(getCertificatesService).get(captor.capture());
            assertEquals(STATUSES, captor.getValue().getStatuses());
        }
    }

    @Nested
    class Response {

        @Test
        void shouldCallConverter() {
            listCertificatesService.get(ListCertificatesRequest.builder().build());

            final var captor =
                    ArgumentCaptor.forClass(se.inera.intyg.minaintyg.integration.api.certificate.dto.Certificate.class);

            verify(certificateConverter, times(1)).convert(captor.capture());

            assertEquals(originalCertificate, captor.getValue());
        }

        @Test
        void shouldSetConvertedCertificateAsContent() {
            final var response = listCertificatesService.get(ListCertificatesRequest.builder().build());

            assertEquals(convertedCertificate, response.getContent().get(0));
        }
    }
}