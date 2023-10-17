package se.inera.intyg.minaintyg.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.certificate.service.GetCertificateListFilterService;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateFilterResponse;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateTypeFilter;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateUnit;

@ExtendWith(MockitoExtension.class)
class FilterControllerTest {

  @Mock
  GetCertificateListFilterService getCertificateListFilterService;

  @InjectMocks
  FilterController filterController;
  private final GetCertificateFilterResponse EXPECTED_RESPONSE = GetCertificateFilterResponse
      .builder()
      .statuses(List.of(CertificateStatusType.SENT))
      .years(List.of("2020"))
      .certificateTypes(List.of(CertificateTypeFilter.builder().build()))
      .units(List.of(CertificateUnit.builder().build()))
      .total(5)
      .build();

  @BeforeEach
  void setup() {
    when(getCertificateListFilterService.get()).thenReturn(EXPECTED_RESPONSE);
  }

  @Nested
  class Response {

    @Test
    void shouldSetCertificateTypes() {
      final var response = filterController.getCertificateListFilter();

      assertEquals(EXPECTED_RESPONSE.getCertificateTypes(), response.getCertificateTypes());
    }

    @Test
    void shouldSetYears() {
      final var response = filterController.getCertificateListFilter();

      assertEquals(EXPECTED_RESPONSE.getYears(), response.getYears());
    }

    @Test
    void shouldSetUnits() {
      final var response = filterController.getCertificateListFilter();

      assertEquals(EXPECTED_RESPONSE.getUnits(), response.getUnits());
    }

    @Test
    void shouldSetStatuses() {
      final var response = filterController.getCertificateListFilter();

      assertEquals(EXPECTED_RESPONSE.getStatuses(), response.getStatuses());
    }

    @Test
    void shouldSetTotal() {
      final var response = filterController.getCertificateListFilter();

      assertEquals(EXPECTED_RESPONSE.getTotal(), response.getTotal());
    }
  }
}