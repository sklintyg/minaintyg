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
package se.inera.intyg.minaintyg.integration.intygsadmin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.banner.model.Banner;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.GetBannersFromIntygsadminService;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;

@ExtendWith(MockitoExtension.class)
class IntygsadminBannerIntegrationServiceTest {

  private static final List<BannerDTO> INTYGSADMIN_RESPONSE = List.of(BannerDTO.builder().build());
  private static final List<Banner> CONVERTED_BANNERS = List.of(Banner.builder().build());
  @Mock private BannerConverter bannerConverter;

  @Mock private GetBannersFromIntygsadminService bannersFromIntygsadminService;
  @Mock private BannerFilterService bannerFilterService;

  @InjectMocks private IntygsadminBannerIntegrationService intygsadminBannerIntegrationService;

  @Test
  void shouldReturnListOfBannersIntygsadmin() {
    final var expectedResponse =
        GetBannerIntegrationResponse.builder().banners(CONVERTED_BANNERS).build();

    when(bannersFromIntygsadminService.get()).thenReturn(INTYGSADMIN_RESPONSE);
    when(bannerFilterService.filter(INTYGSADMIN_RESPONSE)).thenReturn(INTYGSADMIN_RESPONSE);
    when(bannerConverter.convert(INTYGSADMIN_RESPONSE)).thenReturn(CONVERTED_BANNERS);

    final var result = intygsadminBannerIntegrationService.get();

    assertEquals(expectedResponse, result);
  }
}
