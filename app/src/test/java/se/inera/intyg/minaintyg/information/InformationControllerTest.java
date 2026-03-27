/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.minaintyg.information;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;
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

  private static final List<FormattedBanner> EXPECTED_BANNERS =
      List.of(FormattedBanner.builder().build());

  private static final List<DynamicLinkDTO> EXPECTED_LINKS =
      List.of(DynamicLinkDTO.builder().build());

  @Mock GetBannersService getBannersService;
  @Mock GetDynamicLinksService getDynamicLinksService;

  @InjectMocks InformationController informationController;

  @Test
  void shouldReturnConfigResponseWithBanners() {
    final var expected =
        InformationResponseDTO.builder()
            .banners(EXPECTED_BANNERS)
            .links(Collections.emptyList())
            .build();
    when(getBannersService.get()).thenReturn(EXPECTED_BANNERS);

    final var response = informationController.getInformation();

    assertEquals(expected, response);
  }

  @Test
  void shouldReturnConfigResponseWithExpectedLinks() {
    final var expected =
        InformationResponseDTO.builder()
            .links(EXPECTED_LINKS)
            .banners(Collections.emptyList())
            .build();
    when(getDynamicLinksService.get()).thenReturn(EXPECTED_LINKS);

    final var response = informationController.getInformation();

    assertEquals(expected, response);
  }
}
