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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.ApplicationDTO;
import se.inera.intyg.minaintyg.integration.intygsadmin.client.dto.BannerDTO;

@ExtendWith(MockitoExtension.class)
class BannerFilterServiceTest {

  @InjectMocks private BannerFilterService bannerFilterService;

  @Test
  void shouldFilterOnNullValuesForApplication() {
    final var bannerDTOS =
        List.of(
            BannerDTO.builder()
                .displayFrom(LocalDateTime.now().minusDays(1))
                .displayTo(LocalDateTime.now().plusDays(2))
                .build());

    final var result = bannerFilterService.filter(bannerDTOS);

    assertTrue(result.isEmpty());
  }

  @Test
  void shouldFilterOnNullValuesForDisplayFromAndTo() {
    final var bannerDTOS =
        List.of(BannerDTO.builder().application(ApplicationDTO.MINA_INTYG).build());

    final var result = bannerFilterService.filter(bannerDTOS);

    assertTrue(result.isEmpty());
  }

  @Test
  void shouldFilterOnApplicationName() {

    final var expectedBanner =
        BannerDTO.builder()
            .application(ApplicationDTO.MINA_INTYG)
            .displayFrom(LocalDateTime.now())
            .displayTo(LocalDateTime.now().plusMinutes(10))
            .build();

    final var bannerDTOS =
        List.of(
            BannerDTO.builder().build(),
            BannerDTO.builder()
                .application(ApplicationDTO.MINA_INTYG)
                .displayFrom(expectedBanner.getDisplayFrom())
                .displayTo(expectedBanner.getDisplayTo())
                .build());

    final var result = bannerFilterService.filter(bannerDTOS);

    assertEquals(expectedBanner, result.get(0));
  }

  @Test
  void shouldFilterOnDisplayFrom() {
    final var expectedBanner =
        BannerDTO.builder()
            .application(ApplicationDTO.MINA_INTYG)
            .displayFrom(LocalDateTime.now().minusDays(5))
            .displayTo(LocalDateTime.now().plusDays(10))
            .build();

    final var bannerDTOS =
        List.of(
            BannerDTO.builder()
                .application(ApplicationDTO.MINA_INTYG)
                .displayFrom(LocalDateTime.now().plusDays(1))
                .displayTo(LocalDateTime.now().plusDays(10))
                .build(),
            expectedBanner);

    final var result = bannerFilterService.filter(bannerDTOS);

    assertEquals(expectedBanner, result.get(0));
  }

  @Test
  void shouldFilterOnDisplayTo() {
    final var expectedBanner =
        BannerDTO.builder()
            .application(ApplicationDTO.MINA_INTYG)
            .displayFrom(LocalDateTime.now().minusDays(5))
            .displayTo(LocalDateTime.now().plusDays(10))
            .build();

    final var bannerDTOS =
        List.of(
            BannerDTO.builder()
                .application(ApplicationDTO.MINA_INTYG)
                .displayFrom(LocalDateTime.now().minusDays(1))
                .displayTo(LocalDateTime.now().minusDays(1))
                .build(),
            expectedBanner);

    final var result = bannerFilterService.filter(bannerDTOS);

    assertEquals(expectedBanner, result.get(0));
  }
}
