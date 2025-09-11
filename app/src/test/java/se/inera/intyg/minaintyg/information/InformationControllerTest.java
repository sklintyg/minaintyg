package se.inera.intyg.minaintyg.information;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.information.dto.FormattedBanner;
import se.inera.intyg.minaintyg.information.dto.FormattedDynamicLink;
import se.inera.intyg.minaintyg.information.dto.InformationResponseDTO;
import se.inera.intyg.minaintyg.information.service.GetBannersService;
import se.inera.intyg.minaintyg.information.service.GetEnvironmentConfigService;

@ExtendWith(MockitoExtension.class)
class InformationControllerTest {

  private static final List<FormattedBanner> EXPECTED_BANNERS = List.of(
      FormattedBanner.builder().build());
  private static final Map<String, FormattedDynamicLink> EXPECTED_LINKS = Collections.emptyMap();

  private static final InformationResponseDTO EXPECTED_RESPONSE = InformationResponseDTO.builder()
      .banners(EXPECTED_BANNERS)
      .build();

  @Mock
  GetBannersService getBannersService;
  @Mock
  GetEnvironmentConfigService getEnvironmentConfigService;

  @InjectMocks
  InformationController informationController;

  @Test
  void shouldReturnConfigResponseWithBanners() {
    when(getBannersService.get()).thenReturn(EXPECTED_BANNERS);

    final var response = informationController.getInformation();

    assertEquals(EXPECTED_RESPONSE, response);
  }

  @Test
  void shouldReturnConfigResponseWithEnvironmentLinks() {
    when(getEnvironmentConfigService.get()).thenReturn(Collections.emptyMap());

    final var response = informationController.getInformation();

    assertEquals(EXPECTED_LINKS, response.getLinks());
  }

  @Test
  void shouldReturnConfigResponseWithBannersAndEnvironmentLinks() {

    final var expectedResponse = InformationResponseDTO.builder()
        .banners(EXPECTED_BANNERS)
        .links(Collections.emptyMap())
        .build();

    when(getBannersService.get()).thenReturn(EXPECTED_BANNERS);
    when(getEnvironmentConfigService.get()).thenReturn(Collections.emptyMap());

    final var response = informationController.getInformation();

    assertEquals(expectedResponse, response);
  }
}