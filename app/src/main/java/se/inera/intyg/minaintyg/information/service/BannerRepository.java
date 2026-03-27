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
package se.inera.intyg.minaintyg.information.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.config.RedisConfig;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationResponse;
import se.inera.intyg.minaintyg.integration.api.banner.GetBannerIntegrationService;
import se.inera.intyg.minaintyg.integration.common.IntegrationServiceException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BannerRepository {

  private final ObjectMapper objectMapper;
  private final CacheManager cacheManager;
  private final GetBannerIntegrationService getBannerIntegrationService;

  public GetBannerIntegrationResponse get() {
    return getBannerResponseFromCache().orElseGet(this::load);
  }

  public GetBannerIntegrationResponse load() {
    try {
      final var getBannerIntegrationResponse = getBannerIntegrationService.get();

      Objects.requireNonNull(cacheManager.getCache(RedisConfig.BANNERS_CACHE))
          .put(
              RedisConfig.BANNERS_CACHE_KEY,
              objectMapper.writeValueAsString(getBannerIntegrationResponse));

      return getBannerIntegrationResponse;

    } catch (IntegrationServiceException exception) {
      log.error(
          "Unable to establish integration with '{}' stacktrace {}",
          exception.getApplicationName(),
          exception.getStackTrace());

      return GetBannerIntegrationResponse.builder().build();

    } catch (JsonProcessingException e) {
      log.warn("Unable to serialize Banners from IA");

      throw new IllegalStateException(e);
    }
  }

  private Optional<GetBannerIntegrationResponse> getBannerResponseFromCache() {
    try {
      final var cacheValue =
          Objects.requireNonNull(cacheManager.getCache(RedisConfig.BANNERS_CACHE))
              .get(RedisConfig.BANNERS_CACHE_KEY, String.class);

      if (cacheValue == null || cacheValue.isEmpty()) {
        return Optional.empty();
      }

      return Optional.of(objectMapper.readValue(cacheValue, GetBannerIntegrationResponse.class));
    } catch (JsonProcessingException e) {
      log.warn("Unable to deserialize Banners from IA");
      throw new IllegalStateException(e);
    }
  }
}
