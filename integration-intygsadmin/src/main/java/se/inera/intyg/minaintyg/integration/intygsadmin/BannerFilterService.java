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
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.ApplicationDTO;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;
import se.inera.intyg.minaintyg.integration.intygsadmin.util.DateUtil;

@Service
public class BannerFilterService {

  public List<BannerDTO> filter(List<BannerDTO> bannerDTOS) {
    return bannerDTOS.stream()
        .filter(BannerFilterService::isCorrectApplication)
        .filter(BannerFilterService::isActive)
        .toList();
  }

  private static boolean isCorrectApplication(BannerDTO bannerDTO) {
    if (bannerDTO.getApplication() == null) {
      return false;
    }
    return bannerDTO.getApplication().equals(ApplicationDTO.MINA_INTYG);
  }

  private static boolean isActive(BannerDTO bannerDTO) {
    if (bannerDTO.getDisplayFrom() == null || bannerDTO.getDisplayTo() == null) {
      return false;
    }
    return DateUtil.beforeOrEquals(bannerDTO.getDisplayFrom())
        && DateUtil.afterOrEquals(bannerDTO.getDisplayTo());
  }
}
