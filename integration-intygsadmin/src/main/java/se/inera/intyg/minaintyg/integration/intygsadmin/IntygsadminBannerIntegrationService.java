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

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationService;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.GetBannersFromIntygsadminService;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;

@Service
@RequiredArgsConstructor
public class IntygsadminBannerIntegrationService implements GetBannerIntegrationService {

  private final GetBannersFromIntygsadminService getBannersFromIntygsadminService;
  private final BannerConverter bannerConverter;
  private final BannerFilterService bannersFilterService;

  @Override
  public GetBannerIntegrationResponse get() {
    return GetBannerIntegrationResponse.builder()
        .banners(bannerConverter.convert(getActiveBannersFromIntygsadmin()))
        .build();
  }

  private List<BannerDTO> getActiveBannersFromIntygsadmin() {
    final var banners = getBannersFromIntygsadminService.get();
    return bannersFilterService.filter(banners);
  }
}
