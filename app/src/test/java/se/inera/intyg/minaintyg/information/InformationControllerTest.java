package se.inera.intyg.minaintyg.information;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.information.dto.DynamicLinkDTO;
import se.inera.intyg.minaintyg.information.dto.FormattedBanner;
import se.inera.intyg.minaintyg.information.dto.InformationResponseDTO;
import se.inera.intyg.minaintyg.information.service.GetBannersService;
import se.inera.intyg.minaintyg.information.service.GetDynamicLinksService;

@ExtendWith(MockitoExtension.class)
class InformationControllerTest {

  private static final List<FormattedBanner> EXPECTED_BANNERS = List.of(
      FormattedBanner.builder().build());

  private static final List<DynamicLinkDTO> EXPECTED_LINKS = List.of(
      DynamicLinkDTO.builder().build());

  private static final InformationResponseDTO EXPECTED_RESPONSE = InformationResponseDTO.builder()
      .banners(EXPECTED_BANNERS)
      .links(EXPECTED_LINKS)
      .build();

  @Mock
  GetBannersService getBannersService;
  @Mock
  GetDynamicLinksService getDynamicLinksService;

  @InjectMocks
  InformationController informationController;

  @Test
  void shouldReturnConfigResponseWithBanners() {
    when(getDynamicLinksService.get()).thenReturn(EXPECTED_LINKS);
    when(getBannersService.get()).thenReturn(EXPECTED_BANNERS);

    final var response = informationController.getInformation();

    assertEquals(EXPECTED_RESPONSE, response);
  }

  @Test
  void shouldReturnConfigResponseWithExpectedLinks() {
    when(getBannersService.get()).thenReturn(EXPECTED_BANNERS);
    when(getDynamicLinksService.get()).thenReturn(EXPECTED_LINKS);

    final var response = informationController.getInformation();

    assertEquals(EXPECTED_RESPONSE, response);
  }
}