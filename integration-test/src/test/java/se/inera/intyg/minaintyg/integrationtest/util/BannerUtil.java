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
package se.inera.intyg.minaintyg.integrationtest.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.ApplicationDTO;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerPriorityDTO;

public class BannerUtil {

  public static BannerDTO activeBanner(BannerPriorityDTO priority, String message) {
    return BannerDTO.builder()
        .id(UUID.randomUUID())
        .application(ApplicationDTO.MINA_INTYG)
        .createdAt(LocalDateTime.now(ZoneId.systemDefault()))
        .displayFrom(LocalDateTime.now(ZoneId.systemDefault()).minusHours(1))
        .displayTo(LocalDateTime.now(ZoneId.systemDefault()).plusHours(1))
        .message(message)
        .priority(priority)
        .build();
  }
}
